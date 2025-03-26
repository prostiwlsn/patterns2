using System.Security.Claims;

namespace patterns2_infoauth.Common
{
    public static class ClaimsExtentions
    {
        public static bool WithId(this IEnumerable<Claim> claims, Guid id)
        {
            var calimId = claims.Any(c => c.Type == ClaimTypes.NameIdentifier && Guid.Parse(c.Value) == id);

            return calimId;
        }

        public static bool WithAnyRole(this IEnumerable<Claim> claims)
        {
            var roles = claims.Any(c => c.Type == CustomClaimTypes.Role);

            return roles;
        }

        public static bool WithSessionId(this IEnumerable<Claim> claims, Guid id)
        {
            var calimId = claims.Any(c => c.Type == "sessionId" && Guid.Parse(c.Value) == id);

            return calimId;
        }
    }
}
