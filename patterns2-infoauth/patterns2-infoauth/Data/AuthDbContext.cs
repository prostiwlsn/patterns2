using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using patterns2_infoauth.Common;
using patterns2_infoauth.Model;
using System.Reflection.Metadata;
using System.Text;

namespace patterns2_infoauth.Data
{
    public class AuthDbContext : DbContext
    {
        private readonly IConfiguration _config;

        public AuthDbContext(DbContextOptions<AuthDbContext> options, IConfiguration configuration)
            : base(options)
        {
            _config = configuration;
        }
        public DbSet<UserCredentials> UserCredentials { get; set; }
        public DbSet<UserRole> UserRole { get; set; }
        public DbSet<Session> Sessions { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder) => optionsBuilder
            .UseSeeding((context, _) =>
            {
                var adminPhone = _config.GetSection("DefaultAdmin:Phone").Value;
                var adminName = _config.GetSection("DefaultAdmin:Name").Value;
                var adminPassword = _config.GetSection("DefaultAdmin:Password").Value;
                if (adminPhone == null || adminName == null || adminPassword == null) return;

                var admin = context.Set<UserCredentials>().FirstOrDefault(user => user.Phone == adminPhone);
                if (admin != null) return;

                var adminValues = new UserCredentials
                {
                    Password = CryptoCommon.ComputeSha256Hash(adminPassword),
                    Name = adminName,
                    Phone = adminPhone,
                    Id = Guid.NewGuid()
                };

                context.Set<UserCredentials>().Add(adminValues);

                context.Set<UserRole>().Add(new UserRole
                {
                    UserCredentialsId = adminValues.Id,
                    UserCredentials = adminValues,
                    Role = RoleType.Admin
                });

                context.SaveChanges();
            });

        protected override void OnModelCreating(ModelBuilder builder)
        {
            builder.Entity<UserCredentials>()
                .HasIndex(u => u.Phone)
                .IsUnique();
        }
    }
}
