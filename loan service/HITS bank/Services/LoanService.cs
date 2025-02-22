using HITS_bank.Controllers.Dto;
using HITS_bank.Repositories;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Services;

/// <summary>
/// Сервис кредитов
/// </summary>
public class LoanService : ILoanService
{
    private readonly ILoanRepository _loanRepository;

    public LoanService(ILoanRepository loanRepository)
    {
        _loanRepository = loanRepository;
    }
    
    /// <summary>
    /// Создание тарифа кредита
    /// </summary>
    public async Task CreateTariff(TariffDto tariff)
    {
        await _loanRepository.AddTariff(tariff);
    }
}