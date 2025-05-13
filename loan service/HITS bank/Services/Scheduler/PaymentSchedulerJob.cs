using System.Text;
using System.Text.Json;
using Quartz;
using RabbitMQ.Client;
using Serilog;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Services.Scheduler;

public class PaymentSchedulerJob : IJob
{
    private readonly string _amqpConnectionString;
    private readonly string _queueName;
    private readonly ISchedulerService _schedulerService;
    
    public PaymentSchedulerJob(ISchedulerService schedulerService)
    {
        IConfigurationRoot configurationRoot = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json")
            .Build();
        
        _schedulerService = schedulerService;
        _amqpConnectionString = configurationRoot.GetSection("ConnectionStrings")["BusConnection"] ?? throw new InvalidOperationException();
        _queueName = configurationRoot.GetSection("Queues")["PaymentJobQueue"] ?? throw new InvalidOperationException();
    }
    
    public async Task Execute(IJobExecutionContext context)
    {
        try
        {
            var factory = new ConnectionFactory() { HostName = _amqpConnectionString };

            using var connection = factory.CreateConnection();
            using var channel = connection.CreateModel();

            channel.QueueDeclare(queue: _queueName, durable: true, exclusive: false, autoDelete: true);

            var dailyPayments = await _schedulerService.GetDailyPaymentMessages();
            var message = JsonSerializer.Serialize(dailyPayments);
            var body = Encoding.UTF8.GetBytes(message);

            channel.BasicPublish(
                exchange: "",
                routingKey: _queueName,
                basicProperties: null,
                body: body);

            await Task.CompletedTask;
        }
        catch (Exception ex)
        {
            Log.ForContext("TraceId", Guid.NewGuid().ToString())
                .Error(ex, ex.Message);
        }
    }
}