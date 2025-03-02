using RabbitMQ.Client.Events;
using RabbitMQ.Client;
using patterns2_infoauth.Model;
using System.Text;
using System.Text.Json;

namespace patterns2_infoauth.Handlers
{
    public class RpcServer : IDisposable
    {
        private readonly IServiceProvider _serviceProvider;
        private readonly IConnection _connection;
        private readonly IModel _channel;

        private const string _userInfoQueueName = "userinfo";
        private const string _sessionStatusQueueName = "sessionstatus";

        public RpcServer(IServiceProvider serviceProvider, string rabbitMqConnectionString)
        {
            _serviceProvider = serviceProvider;

            var factory = new ConnectionFactory() { Uri = new Uri(rabbitMqConnectionString) };
            _connection = factory.CreateConnection();
            _channel = _connection.CreateModel();

            // Declare the RPC queues.
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
            // Consumer for GetUser requests.
            var userConsumer = new EventingBasicConsumer(_channel);
            userConsumer.Received += async (model, ea) =>
            {
                var props = ea.BasicProperties;
                var replyProps = _channel.CreateBasicProperties();
                replyProps.CorrelationId = props.CorrelationId;

                GetUserResponse response;
                try
                {
                    var body = ea.Body.ToArray();
                    var message = Encoding.UTF8.GetString(body);
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
                    var responseBytes = Encoding.UTF8.GetBytes(JsonSerializer.Serialize(response));
                    _channel.BasicPublish(
                        exchange: "",
                        routingKey: props.ReplyTo,
                        basicProperties: replyProps,
                        body: responseBytes);
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Error publishing GetUser response: " + ex.Message);
                }

                try
                {
                    _channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Error acknowledging GetUser message: " + ex.Message);
                }
            };

            _channel.BasicConsume(queue: _userInfoQueueName, autoAck: false, consumer: userConsumer);

            // Consumer for GetSessionStatus requests.
            var sessionConsumer = new EventingBasicConsumer(_channel);
            sessionConsumer.Received += async (model, ea) =>
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
            };

            _channel.BasicConsume(queue: _sessionStatusQueueName, autoAck: false, consumer: sessionConsumer);
        }

        public void Dispose()
        {
            _channel?.Close();
            _connection?.Close();
        }
    }

}
