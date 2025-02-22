using EasyNetQ;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.IdentityModel.Tokens;
using patterns2_infoauth.Common;
using patterns2_infoauth.Data;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Migrations;
using patterns2_infoauth.Model;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

namespace patterns2_infoauth.Services
{
    public class AuthService : IAuthService
    {
        private AuthDbContext _dbContext;
        private IConfiguration _config;
        public AuthService(AuthDbContext dbContext, IConfiguration config) 
        {
            _dbContext = dbContext;
            _config = config;
        }
        public async Task<string> Register(UserCredentialsDto model)
        {

            try
            {
                Guid id = Guid.NewGuid();

                if (_dbContext.UserCredentials.Any(creds => creds.Phone == model.Phone))
                {
                    Console.WriteLine("this user exists");
                    throw new ArgumentException();
                }

                _dbContext.UserCredentials.Add(new UserCredentials
                {
                    Id = id,
                    Name = model.Name,
                    Phone = model.Phone,
                    Password = CryptoCommon.ComputeSha256Hash(model.Password),
                });

                var saveTask = _dbContext.SaveChangesAsync();

                string key = _config.GetSection("Jwt:Key").Value;
                string issuer = _config.GetSection("Jwt:Issuer").Value;
                string audience = _config.GetSection("Jwt:Audience").Value;

                var crypto = new CryptoCommon(key, issuer, audience);

                await saveTask;

                return await crypto.GenerateAccessToken(id);
            }
            catch
            {
                throw;
            }
        }

        public async Task<string> Login(LoginDto model)
        {
            var user = _dbContext.UserCredentials.FirstOrDefault(creds => creds.Phone == model.Phone && creds.Password == CryptoCommon.ComputeSha256Hash(model.Password));
            if (user == null) throw new ArgumentException();

            try
            {
                string key = _config.GetSection("Jwt:Key").Value;
                string issuer = _config.GetSection("Jwt:Issuer").Value;
                string audience = _config.GetSection("Jwt:Audience").Value;

                var crypto = new CryptoCommon(key, issuer, audience);

                return await crypto.GenerateAccessToken(user.Id);
            }
            catch
            {
                throw;
            }
        }
    }
}
