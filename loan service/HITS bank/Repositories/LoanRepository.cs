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
    /// Получение тарифа
    /// </summary>
    public async Task<TariffEntity?> GetTariff(Guid tariffId)
    {
        return await _context.Tariffs.FirstOrDefaultAsync(x => x.Id == tariffId);
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

    /// <summary>
    /// Добавление кредита
    /// </summary>
    public async Task AddLoan(LoanEntity loanEntity)
    {
        loanEntity.DocumentNumber = _context.Loans.Count() + 1;

        _context.Loans.Add(loanEntity);

        await _context.SaveChangesAsync();
    }

    /// <summary>
    /// Получение списка кредитов пользователя
    /// </summary>
    public async Task<List<LoanEntity>> GetUserLoansList(Guid userId, int offset = 0, int limit = 20)
    {
        return await _context.Loans.Where(x => x.UserId == userId).Skip(offset).Take(limit).ToListAsync();
    }

    /// <summary>
    /// Получение полной истории кредитов пользователя
    /// </summary>
    public async Task<List<LoanEntity>> GetAllUserLoansList(Guid userId)
    {
        return await _context.Loans.Where(x => x.UserId == userId).ToListAsync();
    }

    /// <summary>
    /// Получение кредита
    /// </summary>
    public async Task<LoanEntity?> GetLoan(Guid loanId)
    {
        var result = await _context.Loans.FirstOrDefaultAsync(x => x.Id == loanId);

        return result;
    }

    /// <summary>
    /// Обновление кредита
    /// </summary>
    public async Task<IResult> UpdateLoan(LoanEntity updatedLoanEntity)
    {
        var loan = await _context.Loans.FirstOrDefaultAsync(x => x.Id == updatedLoanEntity.Id);

        if (loan == null)
            return new Error(StatusCodes.Status404NotFound, "Loan not found");

        _context.Entry(loan).CurrentValues.SetValues(updatedLoanEntity);
        await _context.SaveChangesAsync();

        return new Success();
    }

    /// <summary>
    /// Получение списка кредитов
    /// </summary>
    public async Task<List<LoanEntity>> GetLoansList()
    {
        return await _context.Loans.ToListAsync();
    }

    /// <summary>
    /// Повышение долга
    /// </summary>
    public async Task IncreaseDebt()
    {
        await _context.Loans.ForEachAsync(x => x.Debt = x.Debt * x.RatePercent);
    }
}