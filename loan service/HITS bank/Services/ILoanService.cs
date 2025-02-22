using HITS_bank.Controllers.Dto;
#pragma warning disable 1591

namespace HITS_bank.Services;

/// <summary>
/// Сервис кредитов
/// </summary>
public interface ILoanService
{
    Task CreateTariff(TariffDto tariff);
}