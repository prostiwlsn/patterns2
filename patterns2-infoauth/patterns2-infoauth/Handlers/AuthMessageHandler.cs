using EasyNetQ;
using patterns2_infoauth.Common;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Model;
using patterns2_infoauth.Services;
using System.Security.Claims;

namespace patterns2_infoauth.Handlers
{
    public class AuthMessageHandler
    {
        private readonly IUserService _userService;
        private readonly IAuthService _authService;
        private readonly IConfiguration _config;

        public AuthMessageHandler(IUserService userService, IAuthService authService, IConfiguration config)
        {
            _userService = userService;
            _authService = authService;
            _config = config;
        }

        public async Task<GetUserResponse> HandleGetUser(GetUserRequest request)
        {
            try
            {
                var claims = GetClaims(request.TokenString);
                Console.WriteLine(request.TokenString);

                if (!claims.WithId(request.Id) && !claims.WithAnyRole()) 
                    return new GetUserResponse
                    {
                        Success = false
                    };

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
                var claims = GetClaims(request.TokenString);

                if (!claims.WithSessionId(request.SessionId))
                    return new GetSessionStatusResponse { IsSessionActive = false };

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

        private IEnumerable<Claim> GetClaims(string tokenString)
        {
            string key = _config.GetSection("Jwt:Key").Value;
            string issuer = _config.GetSection("Jwt:Issuer").Value;
            string audience = _config.GetSection("Jwt:Audience").Value;
            var cryptoCommon = new CryptoCommon(key, issuer, audience);
            var claims = cryptoCommon.GetClaimsFromTokenString(tokenString);

            return claims;
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
