using patterns2_infoauth.Model;

namespace patterns2_infoauth.Interfaces
{
    public interface IAuthService
    {
        Task<TokenDto> Register(UserCredentialsDto model);
        Task<TokenDto> Login(LoginDto model);
        Task<TokenDto> Refresh(Guid sessionId);
        Task Logout(Guid sessionId);
        Task<bool> IsSessionActive(Guid sessionId);
    }
}
