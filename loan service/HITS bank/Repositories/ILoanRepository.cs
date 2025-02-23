using HITS_bank.Data.Entities;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Repositories;

public interface ILoanRepository
{
    /// <summary>
    /// Добавление тарифа кредита
    /// </summary>
    Task AddTariff(TariffEntity createTariffRequest);
    
    /// <summary>
    /// Обновление тарифа
    /// </summary>
    Task<Utils.IResult> UpdateTariff(TariffEntity updateTariffRequest, Guid tariffId);
    
    /// <summary>
    /// Удаление тарифа
    /// </summary>
    Task<Utils.IResult> DeleteTariff(Guid tariffId);
    
    /// <summary>
    /// Получение списка тарифов
    /// </summary>
    Task<List<TariffEntity>> GetTariffs(int offset = 0, int limit = 20);
}