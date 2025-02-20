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
            var users = _dbContext.UserCredentials.Where(user => user.Name.Contains(name) && user.Phone.Contains(phone)).Select(user => new UserInfoDto { Phone = user.Phone, Name = user.Name, Id = user.Id }).ToList();

            return users;
        }

        public async Task<UserInfoDto> GetUser(Guid id)
        {
            var user = _dbContext.UserCredentials.Find(id);
            if (user == null) throw new ArgumentException();

            return new UserInfoDto { Phone = user.Phone, Name = user.Name, Id = user.Id };
        }



        public async Task CreateUser(UserCreationDto model)
        {
            var user = new UserCredentials
            {
                Id = Guid.NewGuid(),
                Name = model.Name,
                Phone = model.Phone,
                Password = AuthService.ComputeSha256Hash(model.Password)
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
