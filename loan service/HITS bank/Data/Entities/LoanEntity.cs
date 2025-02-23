using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Data.Entities;

/// <summary>
/// Сущность кредита
/// </summary>
public class LoanEntity
{
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid Id { get; init; }
    
    public int DocumentNumber { get; set; }
    
    public Guid UserId { get; init; }
    
    public int Amount { get; init; }
    
    public int Debt { get; init; }
    
    public double RatePercent { get; set; }
    
    public DateTime IssueDate { get; init; }
    
    public DateTime EndDate { get; init; }
}