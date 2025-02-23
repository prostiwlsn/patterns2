using HITS_bank.Controllers.Dto.Response;
using HITS_bank.Data;
using HITS_bank.Data.Entities;
using HITS_bank.Utils;
using Microsoft.EntityFrameworkCore;
using IResult = HITS_bank.Utils.IResult;

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
    /// Удаление тарифа
    /// </summary>
    public async Task<IResult> DeleteTariff(Guid tariffId)
    {
        var tariffEntity = await _context.Tariffs.FirstOrDefaultAsync(x => x.Id == tariffId);
        
        if (tariffEntity == null)
            return new Error(StatusCodes.Status404NotFound, "Tariff not found");
        
        _context.Tariffs.Remove(tariffEntity);
        await _context.SaveChangesAsync();
        
        return new Success();
    }

    /// <summary>
    /// Обновление тарифа
    /// </summary>
    public async Task<IResult> UpdateTariff(TariffEntity updateTariffRequest, Guid tariffId)
    {
        var tariff = await _context.Tariffs.FirstOrDefaultAsync(x => x.Id == tariffId);

        if (tariff == null)
            return new Error(StatusCodes.Status404NotFound, "Tariff not found");

        _context.Entry(tariff).CurrentValues.SetValues(updateTariffRequest);
        await _context.SaveChangesAsync();

        return new Success();
    }

    /// <summary>
    /// Получение списка тарифов
    /// </summary>
    public async Task<List<TariffEntity>> GetTariffs(int offset = 0, int limit = 20)
    {
        return await _context.Tariffs.Skip(offset).Take(limit).ToListAsync();
    }
}