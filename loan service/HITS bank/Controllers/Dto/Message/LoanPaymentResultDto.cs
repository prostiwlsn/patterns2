#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Message;

/// <summary>
/// Результат погашения кредита
/// </summary>
public class LoanPaymentResultDto
{
    public Guid SenderAccountId { get; set; }
    
    public double ReturnedAmount { get; set; }
    
    public string? ErrorMessage { get; set; }
    
    public int? ErrorStatusCode { get; set; }
}

public class LoanPaymentResultData
{
    public Guid SenderAccountId { get; set; }

    public double ReturnedAmount { get; set; }
    
    public bool IsPaymentExpired { get; set; }
}

public class LoanPaymentResultMessage
{
    public bool Success { get; set; }
    public LoanPaymentResultData? Data { get; set; } = null;
    public string? ErrorMessage { get; set; }

    public int? ErrorStatusCode { get; set; }
}