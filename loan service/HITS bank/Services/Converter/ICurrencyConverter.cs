using HITS_bank.Controllers.Dto.Message.AccountDto;

namespace HITS_bank.Services.Converter;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public interface ICurrencyConverter
{
    public Task<double?> Convert(CurrencyEnum from, CurrencyEnum to, double amount);
}