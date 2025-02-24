using EasyNetQ;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Model;
using patterns2_infoauth.Services;

namespace patterns2_infoauth.Handlers
{
    public class AuthMessageHandler
    {
        private readonly IUserService _userService;

        public AuthMessageHandler(IUserService authenticationService)
        {
            _userService = authenticationService;
        }

        public async Task<GetUserResponse> HandleGetUser(GetUserRequest request)
        {
            try
            {
                var user = await _userService.GetUser(request.Id);
                return new GetUserResponse
                {
                    Success = true,
                    Data = user
                };
            }
            catch
            {
                return new GetUserResponse
                {
                    Success = false
                };
            }
        }

        public static async Task<GetUserResponse> GetRpcResponse(WebApplication app, GetUserRequest request)
        {
            //app.Services.CreateScope().ServiceProviderс
            using (var scope = app.Services.CreateScope())
            {
                return await scope.ServiceProvider.GetRequiredService<AuthMessageHandler>().HandleGetUser(request);
            }
        }
    }

    public static class IBusExtensions
    {
        public static async Task SetupListeners(this IBus bus, WebApplication app)
        {
            await bus.Rpc.RespondAsync<GetUserRequest, GetUserResponse>(async (request, cancellationToken) => await AuthMessageHandler.GetRpcResponse(app, request), x => x.WithQueueName("userinfo"));
        }
    }
}
