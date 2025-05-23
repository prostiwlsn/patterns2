#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Message;

/// <summary>
/// Получение суммы кредита
/// </summary>
public class GetLoanDto
{
    public Guid AccountId { get; set; }
    
    public double Amount { get; set; }
    
    public string TractId { get; set; }
}