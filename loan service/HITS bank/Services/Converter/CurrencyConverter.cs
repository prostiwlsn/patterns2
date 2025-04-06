using System.Diagnostics;
using System.Text.Json;
using HITS_bank.Controllers.Dto.External;
using HITS_bank.Controllers.Dto.Message.AccountDto;

namespace HITS_bank.Services.Converter;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class CurrencyConverter : ICurrencyConverter
{
    public async Task<double?> Convert(CurrencyEnum from, CurrencyEnum to, double amount)
    {
        var currencyRates = await GetExchangeRateApiDto(GetCurrencyString(to));
        
        if (currencyRates == null)
            return null;
        
        return amount / currencyRates.Rates[GetCurrencyString(from)];
    }

    private string GetCurrencyString(CurrencyEnum currency)
    {
        if (currency == CurrencyEnum.Usd)
            return "USD";
        else if (currency == CurrencyEnum.Rub)
            return "RUB";
        else
            return "AMD";
    }
    
    private async Task<ExchangeRateApiDto?> GetExchangeRateApiDto(string currency)
    {
        var url = $"https://api.exchangerate-api.com/v4/latest/{currency}";
        var client = new HttpClient();

        var response = await client.GetStringAsync(url);
        var exchangeRates = JsonSerializer.Deserialize<ExchangeRateApiDto>(response);

        return exchangeRates;
    }
}