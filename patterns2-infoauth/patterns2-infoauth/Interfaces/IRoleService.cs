using patterns2_infoauth.Model;

namespace patterns2_infoauth.Interfaces
{
    public interface IRoleService
    {
        Task AddRole(Guid userId, RoleType role);
        Task RemoveRole(Guid userId, RoleType role);
    }
}
