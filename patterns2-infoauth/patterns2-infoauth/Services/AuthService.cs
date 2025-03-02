using EasyNetQ;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
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
        public async Task<TokenDto> Register(UserCredentialsDto model)
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

                var session = new Session
                {
                    Id = Guid.NewGuid(),
                    UserId = id,
                    IsActive = true,
                    Expires = DateTime.UtcNow.AddMonths(1),
                };
                _dbContext.Sessions.Add(session);

                var saveTask = _dbContext.SaveChangesAsync();

                string key = _config.GetSection("Jwt:Key").Value;
                string issuer = _config.GetSection("Jwt:Issuer").Value;
                string audience = _config.GetSection("Jwt:Audience").Value;

                var crypto = new CryptoCommon(key, issuer, audience);

                await saveTask;

                var accessToken = await crypto.GenerateAccessToken(id, id, new List<Claim>());
                var refreshToken = await crypto.GenerateRefreshToken(session.Id);

                return new TokenDto { AccessToken = accessToken, RefreshToken = refreshToken };
            }
            catch
            {
                throw;
            }
        }

        public async Task<TokenDto> Login(LoginDto model)
        {
            var user = _dbContext.UserCredentials.Include(u => u.UserRoles).FirstOrDefault(creds => creds.Phone == model.Phone && creds.Password == CryptoCommon.ComputeSha256Hash(model.Password));
            if (user == null) throw new ArgumentException();

            try
            {
                string key = _config.GetSection("Jwt:Key").Value;
                string issuer = _config.GetSection("Jwt:Issuer").Value;
                string audience = _config.GetSection("Jwt:Audience").Value;

                var session = new Session
                {
                    Id = Guid.NewGuid(),
                    UserId = user.Id,
                    IsActive = true,
                    Expires = DateTime.UtcNow.AddMonths(1),
                };

                _dbContext.Sessions.Add(session);

                var crypto = new CryptoCommon(key, issuer, audience);

                List<Claim> additionalClaims = new List<Claim>();

                user.UserRoles.ForEach(role =>
                {
                    additionalClaims.Add(new Claim(nameof(role.Role), "True"));
                });

                additionalClaims.Add(new Claim("isBlocked", user.IsBlocked.ToString()));

                var accessToken = await crypto.GenerateAccessToken(user.Id, session.Id, additionalClaims);
                var refreshToken = await crypto.GenerateRefreshToken(session.Id);

                return new TokenDto { AccessToken = accessToken, RefreshToken = refreshToken };
            }
            catch
            {
                throw;
            }
        }

        public async Task Logout(Guid sessionId)
        {
            var session = _dbContext.Sessions.Find(sessionId);
            if (session == null) throw new ArgumentException();

            try
            {
                _dbContext.Sessions.Remove(session);
                _dbContext.SaveChanges();
            }
            catch 
            {
                throw;
            }
        }

        public async Task<TokenDto> Refresh(Guid sessionId)
        {
            var session = await _dbContext.Sessions.FindAsync(sessionId);

            if (session == null)
                throw new ArgumentException();

            var user = await _dbContext.UserCredentials.Include(u => u.UserRoles).FirstOrDefaultAsync(u => u.Id == session.UserId);

            if (user == null)
                throw new ArgumentException();

            Guid newSessionId = Guid.NewGuid();

            try
            {
                string key = _config.GetSection("Jwt:Key").Value;
                string issuer = _config.GetSection("Jwt:Issuer").Value;
                string audience = _config.GetSection("Jwt:Audience").Value;

                List<Claim> additionalClaims = new List<Claim>();

                user.UserRoles.ForEach(role =>
                {
                    additionalClaims.Add(new Claim(nameof(role.Role), "True"));
                });

                additionalClaims.Add(new Claim("isBlocked", user.IsBlocked.ToString()));

                var crypto = new CryptoCommon(key, issuer, audience);
                var accessToken = await crypto.GenerateAccessToken(user.Id, session.Id, additionalClaims);
                var refreshToken = await crypto.GenerateRefreshToken(session.Id);

                Session newSession = new Session
                {
                    Id = newSessionId,
                    UserId = user.Id,
                    IsActive = true,
                    Expires = DateTime.UtcNow.AddMonths(1),
                };

                _dbContext.Sessions.Remove(session);
                _dbContext.Sessions.Add(newSession);
                _dbContext.SaveChanges();

                return new TokenDto { AccessToken = accessToken, RefreshToken = refreshToken };
            }
            catch
            {
                throw;
            }
        }

    }
}
