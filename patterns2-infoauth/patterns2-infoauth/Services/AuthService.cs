using EasyNetQ;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.IdentityModel.Tokens;
using patterns2_infoauth.Data;
using patterns2_infoauth.Migrations;
using patterns2_infoauth.Model;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

namespace patterns2_infoauth.Services
{
    public class AuthService
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
                    throw new ArgumentException();
                }

                _dbContext.UserCredentials.Add(new UserCredentials
                {
                    Id = id,
                    Name = model.Name,
                    Phone = model.Phone,
                    Password = ComputeSha256Hash(model.Password),
                });

                _dbContext.SaveChanges();

                return await GenerateAccessToken(id);
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<string> Login(LoginDto model)
        {
            var user = _dbContext.UserCredentials.First(creds => creds.Phone == model.Phone && creds.Password == ComputeSha256Hash(model.Password));
        }

        public static string ComputeSha256Hash(string rawData)
        {
            using (SHA256 sha256Hash = SHA256.Create())
            {
                byte[] bytes = sha256Hash.ComputeHash(Encoding.UTF8.GetBytes(rawData));

                StringBuilder builder = new StringBuilder();
                foreach (byte b in bytes)
                {
                    builder.Append(b.ToString("x2"));
                }
                return builder.ToString();
            }
        }

        private async Task<string> GenerateAccessToken(Guid id)
        {
            var claims = new List<Claim>
            {
                new Claim("id", id.ToString()),
            };

            return await GenerateToken(claims, DateTime.Now.AddMonths(1));
        }

        private async Task<string> GenerateToken(List<Claim> claims, DateTime expires)
        {
            try
            {
                var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config.GetSection("Jwt:Key").Value));

                var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha512Signature);

                var securityToken = new JwtSecurityToken(
                    claims: claims,
                    expires: expires,
                    signingCredentials: credentials,
                    issuer: _config.GetSection("Jwt:Issuer").Value,
                    audience: _config.GetSection("Jwt:Audience").Value
                );

                var token = new JwtSecurityTokenHandler().WriteToken(securityToken);
                return token;
            }
            catch (Exception ex)
            {
                throw;
            }
        }
    }
}
