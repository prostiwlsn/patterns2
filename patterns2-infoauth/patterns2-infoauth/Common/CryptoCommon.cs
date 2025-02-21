using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

namespace patterns2_infoauth.Common
{
    public class CryptoCommon
    {
        public CryptoCommon(string key, string issuer, string audience) { }

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

        public async Task<string> GenerateAccessToken(Guid id)
        {
            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.NameIdentifier, id.ToString()),
            };

            return await GenerateToken(claims, DateTime.Now.AddMonths(1));
        }

        public async Task<string> GenerateToken(List<Claim> claims, DateTime expires)
        {
            try
            {
                var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Key));

                var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha512Signature);

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
    }
}
