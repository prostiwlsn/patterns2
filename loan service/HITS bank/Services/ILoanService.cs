using HITS_bank.Controllers.Dto;
using HITS_bank.Controllers.Dto.Common;
using HITS_bank.Controllers.Dto.Response;
using IResult = HITS_bank.Utils.IResult;

#pragma warning disable 1591

namespace HITS_bank.Services;

/// <summary>
/// Сервис кредитов
/// </summary>
public interface ILoanService
{
    /// <summary>
    /// Создание тарифа кредита
    /// </summary>
    Task CreateTariff(CreateTariffRequestDto createTariffRequest);
    
    /// <summary>
    /// Удаление тарифа кредита
    /// </summary>
    Task<IResult> DeleteTariff(Guid tariffId);
    
    /// <summary>
    /// Получение списка кредитов
    /// </summary>
    Task<IResult> GetTariffs(int pageNumber, int pageSize);
}