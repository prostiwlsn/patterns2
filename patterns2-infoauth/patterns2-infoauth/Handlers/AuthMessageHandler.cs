using EasyNetQ;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Model;
using patterns2_infoauth.Services;

namespace patterns2_infoauth.Handlers
{
    public class AuthMessageHandler
    {
        private readonly IUserService _userService;
        private readonly IAuthService _authService;

        public AuthMessageHandler(IUserService userService, IAuthService authService)
        {
            _userService = userService;
            _authService = authService;
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

        public async Task<GetSessionStatusResponse> HandleGetSessionStatus(GetSessionStatusRequest request)
        {
            try
            {
                var status = await _authService.IsSessionActive(request.SessionId);
                return new GetSessionStatusResponse { IsSessionActive = status };
            }
            catch
            {
                return new GetSessionStatusResponse { IsSessionActive = false };
            }
        }

        public static async Task<GetUserResponse> GetUserRpcResponse(WebApplication app, GetUserRequest request)
        {
            //app.Services.CreateScope().ServiceProviderс
            using (var scope = app.Services.CreateScope())
            {
                return await scope.ServiceProvider.GetRequiredService<AuthMessageHandler>().HandleGetUser(request);
            }
        }
        public static async Task<GetSessionStatusResponse> GetSessionRpcResponse(WebApplication app, GetSessionStatusRequest request)
        {
            //app.Services.CreateScope().ServiceProviderс
            using (var scope = app.Services.CreateScope())
            {
                return await scope.ServiceProvider.GetRequiredService<AuthMessageHandler>().HandleGetSessionStatus(request);
            }
        }
    }

    public static class IBusExtensions
    {
        public static async Task SetupListeners(this IBus bus, WebApplication app)
        {
            await bus.Rpc.RespondAsync<GetUserRequest, GetUserResponse>(async (request, cancellationToken) => await AuthMessageHandler.GetUserRpcResponse(app, request), x => x.WithQueueName("userinfoxd"));
        }
    }
}
