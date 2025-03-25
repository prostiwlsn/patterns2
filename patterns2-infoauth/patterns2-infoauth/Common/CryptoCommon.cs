using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json.Linq;
using patterns2_infoauth.Model;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

namespace patterns2_infoauth.Common
{
    public class CryptoCommon
    {
        public CryptoCommon(string key, string issuer, string audience) 
        {
            Key = key;
            Issuer = issuer;
            Audience = audience;
        }

        public string Key { get; set; }
        public string Issuer { get; set; }
        public string Audience { get; set; }

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

        public async Task<string> GenerateAccessToken(Guid id, Guid sessionId, List<Claim> additionalClaims)
        {
            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.NameIdentifier, id.ToString()),
                new Claim("sessionId", sessionId.ToString())
            };

            claims.AddRange(additionalClaims);

            return await GenerateToken(claims, DateTime.Now.AddHours(1));
        }

        public async Task<string> GenerateToken(List<Claim> claims, DateTime expires)
        {
            try
            {
                Console.WriteLine(Key);
                var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Key));

                var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

                var securityToken = new JwtSecurityToken(
                    claims: claims,
                    expires: expires,
                    signingCredentials: credentials,
                    issuer: Issuer,
                    audience: Audience
                );

                var token = new JwtSecurityTokenHandler().WriteToken(securityToken);
                return token;
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<string> GenerateRefreshToken(Guid sessionId)
        {
            var claims = new List<Claim>
            {
                new Claim("type", "refresh"),
                new Claim("sessionId", sessionId.ToString())
            };

            return await GenerateToken(claims, DateTime.Now.AddMonths(1));
        }

        public IEnumerable<Claim> GetClaimsFromTokenString(string tokenString)
        {
            var handler = new JwtSecurityTokenHandler();

            var validationParameters = new TokenValidationParameters
            {
                ValidateIssuerSigningKey = true, 
                IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Key)),

                ValidateIssuer = false, 
                ValidateAudience = false, 

                ValidateLifetime = true, 
                ClockSkew = TimeSpan.Zero 
            };

            ClaimsPrincipal principal = handler.ValidateToken(tokenString, validationParameters, out SecurityToken validatedToken);

            JwtSecurityToken jwtSecurityToken = handler.ReadJwtToken(tokenString);

            return jwtSecurityToken.Claims;
        }
    }
}
