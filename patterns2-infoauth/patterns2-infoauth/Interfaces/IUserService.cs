using patterns2_infoauth.Model;

namespace patterns2_infoauth.Interfaces
{
    public interface IUserService
    {
        Task<List<UserInfoDto>> GetUsers(string name = "", string phone = "");
        Task<UserInfoDto> GetUser(Guid id);
        Task CreateUser(UserCreationDto model);
        Task EditUser(UserEditDto model, Guid id);
        Task BlockUser(Guid id);
        Task UnblockUser(Guid id);
    }
}
