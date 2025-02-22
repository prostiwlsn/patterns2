using Microsoft.EntityFrameworkCore;
using patterns2_infoauth.Data;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Model;

namespace patterns2_infoauth.Services
{
    public class RoleService : IRoleService
    {
        private AuthDbContext _dbContext;
        public RoleService(AuthDbContext dbContext)
        {
            _dbContext = dbContext;
        }

        public async Task AddRole(Guid userId, RoleType role)
        {
            var user = _dbContext.UserCredentials.Find(userId);
            if (user == null) throw new ArgumentException();

            var userRole = new UserRole
            { 
                UserCredentials = user,
                Role = role,
                UserCredentialsId = userId
            };

            try
            {
                _dbContext.UserRole.Add(userRole);

                await _dbContext.SaveChangesAsync();
            }
            catch
            {
                throw;
            }

        }

        public async Task RemoveRole(Guid userId, RoleType role)
        {
            var user = _dbContext.UserCredentials.Find(userId);
            if (user == null) throw new ArgumentException();

            try
            {
                var userRole = _dbContext.UserRole.Find(role, userId);
                if (userRole == null) throw new ArgumentException();

                _dbContext.UserRole.Remove(userRole);   

                await _dbContext.SaveChangesAsync();
            }
            catch
            {
                throw;
            }
        }
    }
}
