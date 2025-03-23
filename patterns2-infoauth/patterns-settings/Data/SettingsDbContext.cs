using Microsoft.EntityFrameworkCore;
using patterns_settings.Models;

namespace patterns_settings.Data
{
    public class SettingsDbContext : DbContext
    {
        public SettingsDbContext(DbContextOptions<SettingsDbContext> options) : base(options) { }
        public DbSet<AdminClientSettings> AdminClientSettings { get; set; }
        public DbSet<BankClientSettings> BankClientSettings { get; set; }
    }
}
