using HITS_bank.Controllers.Dto;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Repositories;

public interface ILoanRepository
{
    Task AddTariff(TariffDto tariff);
}