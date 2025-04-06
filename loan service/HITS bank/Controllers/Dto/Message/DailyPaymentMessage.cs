namespace HITS_bank.Controllers.Dto.Message;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class DailyPaymentMessage
{
    public Guid AccountId { get; set; }
    
    public double Amount { get; set; }
}