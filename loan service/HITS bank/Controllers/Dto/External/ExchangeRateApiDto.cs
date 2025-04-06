using System.Text.Json.Serialization;

namespace HITS_bank.Controllers.Dto.External;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class ExchangeRateApiDto
{
    [JsonPropertyName("rates")]
    public Dictionary<string, double> Rates { get; set; }

    [JsonPropertyName("base")]
    public string BaseCurrency { get; set; }

    [JsonPropertyName("date")]
    public string Date { get; set; }
}