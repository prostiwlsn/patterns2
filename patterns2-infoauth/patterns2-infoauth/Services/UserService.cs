using patterns2_infoauth.Common;
using patterns2_infoauth.Data;
using patterns2_infoauth.Model;

namespace patterns2_infoauth.Services
{
    public class UserService
    {
        private AuthDbContext _dbContext;
        public UserService(AuthDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        public async Task<List<UserInfoDto>> GetUsers(string name = "", string phone = "")
        {
            var users = _dbContext.UserCredentials.Where(user => user.Name.Contains(name) && user.Phone.Contains(phone)).Select(user => new UserInfoDto 
                { Phone = user.Phone, Name = user.Name, Id = user.Id, IsBlocked = user.IsBlocked, Roles = user.UserRoles.Select(role => role.Role).ToArray() }).ToList();

            return users;
        }

        public async Task<UserInfoDto> GetUser(Guid id)
        {
            var user = _dbContext.UserCredentials.Find(id);
            if (user == null) throw new ArgumentException();

            return new UserInfoDto { Phone = user.Phone, Name = user.Name, Id = user.Id, IsBlocked = user.IsBlocked, Roles = user.UserRoles.Select(role => role.Role).ToArray() };
        }



        public async Task CreateUser(UserCreationDto model)
        {
            var user = new UserCredentials
            {
                Id = Guid.NewGuid(),
                Name = model.Name,
                Phone = model.Phone,
                Password = CryptoCommon.ComputeSha256Hash(model.Password)
            };

            try
            {
                _dbContext.UserCredentials.Add(user);
                await _dbContext.SaveChangesAsync();
            }
            catch (Exception ex)
            {
                throw;
            }

        }

        public async Task EditUser(UserEditDto model, Guid id)
        {
            var user = _dbContext.UserCredentials.Find(id);
            if (user == null) throw new ArgumentException();

            user.Name = model.Name;
            user.Phone = model.Phone;

            await _dbContext.SaveChangesAsync();
        }

        public async Task BlockUser(Guid id)
        {
            var user = _dbContext.UserCredentials.Find(id);
            if (user == null) throw new ArgumentException();

            try
            {
                user.IsBlocked = true;
                await _dbContext.SaveChangesAsync();
            }
            catch
            {
                throw;
            }
        }

        public async Task UnblockUser(Guid id)
        {
            var user = _dbContext.UserCredentials.Find(id);
            if (user == null) throw new ArgumentException();

            try
            {
                user.IsBlocked = false;
                await _dbContext.SaveChangesAsync();
            }
            catch
            {
                throw;
            }
        }
    }
}
