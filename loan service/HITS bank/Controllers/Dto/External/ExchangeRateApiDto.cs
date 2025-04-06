namespace HITS_bank.Controllers.Dto.External;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class ExchangeRateApiDto
{
    public string Base { get; set; }
    public DateTime Date { get; set; }
    public Dictionary<string, double> Rates { get; set; }
}