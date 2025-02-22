using patterns2_infoauth.Model;

namespace patterns2_infoauth.Interfaces
{
    public interface IAuthService
    {
        Task<string> Register(UserCredentialsDto model);
        Task<string> Login(LoginDto model);
    }
}
