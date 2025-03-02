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
        private const string _queueName = "userinfo";

        public RpcServer(IServiceProvider serviceProvider, string rabbitMqConnectionString)
        {
            _serviceProvider = serviceProvider;

            var factory = new ConnectionFactory
            {
                Uri = new Uri(rabbitMqConnectionString)
            };
            _connection = factory.CreateConnection();
            _channel = _connection.CreateModel();

            // Declare the RPC queue.
            _channel.QueueDeclare(
                queue: _queueName,
                durable: true,
                exclusive: false,
                autoDelete: false,
                arguments: null);
        }

        public void Start()
        {
            var consumer = new EventingBasicConsumer(_channel);
            consumer.Received += async (model, ea) =>
            {
                var props = ea.BasicProperties;
                var replyProps = _channel.CreateBasicProperties();
                replyProps.CorrelationId = props.CorrelationId;
                object response = null;

                try
                {
                    var body = ea.Body.ToArray();
                    var message = Encoding.UTF8.GetString(body);

                    // Use JSON document to inspect the incoming message.
                    using var document = JsonDocument.Parse(message);
                    var root = document.RootElement;

                    using (var scope = _serviceProvider.CreateScope())
                    {
                        var handler = scope.ServiceProvider.GetRequiredService<AuthMessageHandler>();

                        // Check for a RequestType discriminator in the JSON.
                        if (root.TryGetProperty("RequestType", out JsonElement typeElement))
                        {
                            var requestType = typeElement.GetString();
                            if (requestType == "GetSessionStatus")
                            {
                                var request = JsonSerializer.Deserialize<GetSessionStatusRequest>(message);
                                response = await handler.HandleGetSessionStatus(request);
                            }
                            else if (requestType == "GetUser")
                            {
                                var request = JsonSerializer.Deserialize<GetUserRequest>(message);
                                response = await handler.HandleGetUser(request);
                            }
                            else
                            {
                                response = new { Success = false, Error = "Unknown request type" };
                            }
                        }
                        else
                        {
                            // Fallback: use available properties to differentiate the request.
                            if (root.TryGetProperty("SessionId", out _))
                            {
                                var request = JsonSerializer.Deserialize<GetSessionStatusRequest>(message);
                                response = await handler.HandleGetSessionStatus(request);
                            }
                            else
                            {
                                var request = JsonSerializer.Deserialize<GetUserRequest>(message);
                                response = await handler.HandleGetUser(request);
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    response = new { Success = false, Error = ex.Message };
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
                    Console.WriteLine("Error publishing response: " + ex.Message);
                }

                try
                {
                    _channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Error sending ACK: " + ex.Message);
                }
            };

            _channel.BasicConsume(queue: _queueName, autoAck: false, consumer: consumer);
        }

        public void Dispose()
        {
            _channel?.Close();
            _connection?.Close();
        }
    }
}
