using RabbitMQ.Client.Events;
using RabbitMQ.Client;
using patterns2_infoauth.Model;
using System.Text;
using System.Text.Json;
using Serilog;
using System.Diagnostics;

namespace patterns2_infoauth.Handlers
{
    public class RpcServer : IDisposable
    {
        private readonly IServiceProvider _serviceProvider;
        private readonly IConnection _connection;
        private readonly IModel _channel;
        private readonly ILogger<RpcServer> _logger;

        private const string _userInfoQueueName = "userinfo";
        private const string _sessionStatusQueueName = "sessionstatus";

        public RpcServer(IServiceProvider serviceProvider, ILogger<RpcServer> logger)
        {
            _serviceProvider = serviceProvider;
            _logger = logger;

            var factory = new ConnectionFactory() { Uri = new Uri(rabbitMqConnectionString) };
            _connection = factory.CreateConnection();
            _channel = _connection.CreateModel();

            _channel.QueueDeclare(
                queue: _userInfoQueueName,
                durable: true,
                exclusive: false,
                autoDelete: false,
                arguments: null);

            _channel.QueueDeclare(
                queue: _sessionStatusQueueName,
                durable: true,
                exclusive: false,
                autoDelete: false,
                arguments: null);
        }

        public void Start()
        {
            var userConsumer = new EventingBasicConsumer(_channel);
            userConsumer.Received += async (model, ea) =>
            {
                await HandleGetUserInfo(ea);
            };

            _channel.BasicConsume(queue: _userInfoQueueName, autoAck: false, consumer: userConsumer);

            var sessionConsumer = new EventingBasicConsumer(_channel);
            sessionConsumer.Received += async (model, ea) =>
            {
                await HandleGetSessionStatus(ea);
            };

            _channel.BasicConsume(queue: _sessionStatusQueueName, autoAck: false, consumer: sessionConsumer);
        }

        private async Task HandleGetUserInfo(BasicDeliverEventArgs ea)
        {
            var props = ea.BasicProperties;
            var replyProps = _channel.CreateBasicProperties();
            replyProps.CorrelationId = props.CorrelationId;
            var stopwatch = Stopwatch.StartNew();

            GetUserResponse response;
            try
            {
                _logger.LogInformation("Started processing AMQP HandleGetUserInfo");

                var body = ea.Body.ToArray();
                var message = Encoding.UTF8.GetString(body);
                Console.WriteLine(message);
                var request = JsonSerializer.Deserialize<GetUserRequest>(message);
                using (var scope = _serviceProvider.CreateScope())
                {
                    var handler = scope.ServiceProvider.GetRequiredService<AuthMessageHandler>();
                    response = await handler.HandleGetUser(request);
                }
            }
            catch
            {
                response = new GetUserResponse { Success = false };
            }

            try
            {
                Random rnd = new Random();
                int result = rnd.Next(1, 100);

                if (result > 50)
                {
                    throw new Exception();
                }

                Console.WriteLine(JsonSerializer.Serialize(response));
                var responseBytes = Encoding.UTF8.GetBytes(JsonSerializer.Serialize(response));
                _channel.BasicPublish(
                    exchange: "",
                    routingKey: props.ReplyTo,
                    basicProperties: replyProps,
                    body: responseBytes);

                stopwatch.Stop();
                _logger.LogInformation("Completed AMQP HandleGetUserInfo after {ResponseTimeMs} ms", stopwatch.ElapsedMilliseconds);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error publishing GetUser response: " + ex.Message);
                stopwatch.Stop();
                _logger.LogError("Error processing AMQP HandleGetUserInfo after {ResponseTimeMs} ms", stopwatch.ElapsedMilliseconds);
            }

            try
            {
                _channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error acknowledging GetUser message: " + ex.Message);
            }
        }

        private async Task HandleGetSessionStatus(BasicDeliverEventArgs ea)
        {
            var props = ea.BasicProperties;
            var replyProps = _channel.CreateBasicProperties();
            replyProps.CorrelationId = props.CorrelationId;

            GetSessionStatusResponse response;
            try
            {
                var body = ea.Body.ToArray();
                var message = Encoding.UTF8.GetString(body);
                var request = JsonSerializer.Deserialize<GetSessionStatusRequest>(message);
                using (var scope = _serviceProvider.CreateScope())
                {
                    var handler = scope.ServiceProvider.GetRequiredService<AuthMessageHandler>();
                    response = await handler.HandleGetSessionStatus(request);
                }
            }
            catch
            {
                response = new GetSessionStatusResponse { IsSessionActive = false };
            }

            try
            {
                var responseBytes = Encoding.UTF8.GetBytes(JsonSerializer.Serialize(response));
                _channel.BasicPublish(
                    exchange: "",
                    routingKey: props.ReplyTo,
                    basicProperties: replyProps,
                    body: responseBytes);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error publishing GetSessionStatus response: " + ex.Message);
            }

            try
            {
                _channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error acknowledging GetSessionStatus message: " + ex.Message);
            }
        }

        private string rabbitMqConnectionString = "amqp://guest:guest@194.59.186.122:5672/";

        public void Dispose()
        {
            _channel?.Close();
            _connection?.Close();
        }
    }

}
