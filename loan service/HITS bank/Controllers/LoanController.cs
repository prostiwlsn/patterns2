using HITS_bank.Controllers.Dto;
using HITS_bank.Services;
using Microsoft.AspNetCore.Mvc;

namespace HITS_bank.Controllers;

/// <summary>
/// Кредиты
/// </summary>
[ApiController]
[Route("api/loan")]
public class LoanController : ControllerBase
{
    private readonly ILoanService _loanService;

    /// <summary>
    /// Конструктор
    /// </summary>
    public LoanController(ILoanService loanService)
    {
        _loanService = loanService;
    }
    
    /// <summary>
    /// Создание тарифа для кредита
    /// </summary>
    [HttpPost]
    [Route("tariff")]
    public async Task CreateLoanTariff(TariffDto tariff)
    {
        await _loanService.CreateTariff(tariff);
    }
}