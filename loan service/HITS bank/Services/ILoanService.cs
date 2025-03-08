using HITS_bank.Controllers.Dto;
using HITS_bank.Controllers.Dto.Message;
using HITS_bank.Controllers.Dto.Request;
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
    /// Обновление тарифа
    /// </summary>
    Task<IResult> UpdateTariff(UpdateTariffRequestDto updatedTariff, Guid tariffId);
    
    /// <summary>
    /// Получение списка кредитов
    /// </summary>
    Task<IResult> GetTariffs(int pageNumber, int pageSize);
    
    /// <summary>
    /// Создание кредита
    /// </summary>
    Task<IResult> CreateLoan(CreateLoanRequestDto createLoanRequest);
    
    /// <summary>
    /// Получение списко кредитов пользователя
    /// </summary>
    Task<IResult> GetUserLoansList(Guid userId, int pageNumber, int pageSize);

    /// <summary>
    /// Оплатить кредит
    /// </summary>
    Task<LoanPaymentResultMessage> PayForLoan(LoanPaymentDto loanPayment);
}