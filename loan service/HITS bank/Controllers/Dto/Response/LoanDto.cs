#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Response;

/// <summary>
/// Кредит
/// </summary>
public class LoanDto
{
    public Guid Id { get; init; }
    
    public int DocumentNumber { get; set; }
    
    public int Amount { get; init; }
    
    public int Debt { get; init; }
    
    public double RatePercent { get; set; }
    
    public bool IsExpired { get; set; }
    
    public DateTime IssueDate { get; init; }
    
    public DateTime EndDate { get; init; }
}