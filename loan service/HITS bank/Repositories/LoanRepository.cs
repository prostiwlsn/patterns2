using HITS_bank.Data;
using HITS_bank.Data.Entities;
using Microsoft.EntityFrameworkCore;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Repositories;

public class LoanRepository : ILoanRepository
{
    private readonly ApplicationDbContext _context;

    public LoanRepository(ApplicationDbContext context)
    {
        _context = context;
    }
    
    /// <summary>
    /// Добавление тарифа кредита
    /// </summary>
    public async Task AddTariff(TariffEntity createTariffRequest)
    { 
      _context.Tariffs.Add(createTariffRequest);
      await _context.SaveChangesAsync();
    }

    /// <summary>
    /// Получение тарифа
    /// </summary>
    public async Task<TariffEntity?> GetTariff(Guid tariffId)
    {
        return await _context.Tariffs.FirstOrDefaultAsync(x => x.Id == tariffId);
    }
    
    /// <summary>
    /// Удаление тарифа
    /// </summary>
    public async Task DeleteTariff(TariffEntity tariff)
    {
        _context.Tariffs.Remove(tariff);
        await _context.SaveChangesAsync();
    }

    /// <summary>
    /// Получение списка тарифов
    /// </summary>
    public async Task<List<TariffEntity>> GetTariffs(int offset = 0, int limit = 20)
    {
        return await _context.Tariffs.Skip(offset).Take(limit).ToListAsync();
    }
}