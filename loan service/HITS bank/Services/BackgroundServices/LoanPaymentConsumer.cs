using System.Text;
using System.Text.Json;
using AutoMapper;
using HITS_bank.Controllers.Dto.Message;
using HITS_bank.Data;
using HITS_bank.Mappers;
using HITS_bank.Repositories;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Services.BackgroundServices;

public class LoanPaymentConsumer : BackgroundService
{
    private readonly IModel _channel;
    private readonly string _queueName;
    private readonly string _loanPaymentResultExchange;
    private readonly string _amqpConnectionString;
    private readonly IServiceScopeFactory _serviceScopeFactory;
    private string? _correlationId;
    
    public LoanPaymentConsumer(IServiceScopeFactory serviceScopeFactory)
    {
        _serviceScopeFactory = serviceScopeFactory;
        
        IConfigurationRoot configurationRoot = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json")
            .Build();
        
        var loanPaymentExchange = configurationRoot.GetSection("Exchanges")["LoanPayment"] ?? throw new InvalidOperationException();
        _loanPaymentResultExchange = configurationRoot.GetSection("Exchanges")["LoanPaymentResponse"] ?? throw new InvalidOperationException();
        _amqpConnectionString = configurationRoot.GetSection("ConnectionStrings")["BusConnection"] ?? throw new InvalidOperationException();

        // Создание очереди сообщений
        var factory = new ConnectionFactory { HostName = _amqpConnectionString };
        var connection = factory.CreateConnection();
        _channel = connection.CreateModel();
        _channel.ExchangeDeclare(exchange: loanPaymentExchange, type: ExchangeType.Direct, durable: true);

        _queueName = "LoanPayment";
        _channel.QueueDeclare(queue: _queueName, durable: true, exclusive: false, autoDelete: false);
        _channel.QueueBind(queue: _queueName,
            exchange: loanPaymentExchange,
            routingKey: "LoanPayment");
        
        _channel.BasicQos(prefetchSize: 0, prefetchCount: 1, global: false);
    }
    
    protected override Task ExecuteAsync(CancellationToken stoppingToken)
    {
        stoppingToken.ThrowIfCancellationRequested();
        
        var consumer = new EventingBasicConsumer(_channel);
        consumer.Received += Consume;
        
        _channel.BasicConsume(_queueName, false, consumer);
        
        return Task.CompletedTask;
    }
    
    // Получение сообщения
    private void Consume(object? ch, BasicDeliverEventArgs eventArgs)
    {
        var content = Encoding.UTF8.GetString(eventArgs.Body.ToArray());
        _correlationId = eventArgs.BasicProperties.CorrelationId;
        _channel.BasicAck(eventArgs.DeliveryTag, false);

        Task.Run(async () =>
        {
            await ReturnAnswer(content);
        }).GetAwaiter().GetResult();
    }
    
    // Возврат ответа на сообщение
    private async Task ReturnAnswer(string content)
    {
        // Получение ответа
        LoanPaymentDto? loanPayment = JsonSerializer.Deserialize<LoanPaymentDto>(content);

        if (loanPayment == null)
            return;
        
        LoanPaymentResultMessage answer;
        
        using (var scope = _serviceScopeFactory.CreateScope())
        {
            var context = scope.ServiceProvider.GetRequiredService<ApplicationDbContext>();
            var mapper = scope.ServiceProvider.GetRequiredService<IMapper>();
            var loanService = new LoanService(
                new LoanRepository(context),
                mapper);
            
            answer = await loanService.PayForLoan(loanPayment);
        }

        // Отправка ответа
        var factory = new ConnectionFactory{ HostName = _amqpConnectionString };
        
        _channel.ExchangeDeclare(exchange: _loanPaymentResultExchange, type: ExchangeType.Direct, durable: true);
        _channel.QueueDeclare(queue: "LoanPaymentResponse", durable: true, exclusive: false, autoDelete: false);
        _channel.QueueBind(queue: "LoanPaymentResponse",
            exchange: _loanPaymentResultExchange,
            routingKey: "LoanPaymentResponse");
        
        using (var connection = factory.CreateConnection())
        using (var channel = connection.CreateModel())
        {
            var message = JsonSerializer.Serialize(answer);
            var body = Encoding.UTF8.GetBytes(message);
            
            var properties = channel.CreateBasicProperties();
            properties.Persistent = true;
            properties.CorrelationId = _correlationId;

            channel.BasicPublish(exchange: _loanPaymentResultExchange,
                routingKey: "LoanPaymentResponse",
                basicProperties: properties,
                body: body);
        }
    }
}