using HITS_bank.Data.Entities;
using Microsoft.EntityFrameworkCore;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Data;

public class ApplicationDbContext : DbContext 
{
    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)
    { }
    
    public DbSet<TariffEntity> Tariffs { get; set; }
    public DbSet<LoanEntity> Loans { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<TariffEntity>().HasKey(tariff => tariff.Id);
        modelBuilder.Entity<LoanEntity>().HasKey(loan => loan.Id);
        
        base.OnModelCreating(modelBuilder);
    }
}