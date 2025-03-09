#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Message;

/// <summary>
/// Погашение кредита
/// </summary>
public class LoanPaymentDto
{
    public Guid SenderAccountId { get; set; }
    
    public Guid RecipientAccountId { get; set; }
    
    public double Amount { get; set; }
}