using Microsoft.EntityFrameworkCore;
using patterns2_infoauth.Model;

namespace patterns2_infoauth.Data
{
    public class AuthDbContext : DbContext
    {
        public AuthDbContext(DbContextOptions<AuthDbContext> options) : base(options) { }
        public DbSet<UserCredentials> UserCredentials { get; set; }
        public DbSet<UserRole> UserRole { get; set; }
    }
}
