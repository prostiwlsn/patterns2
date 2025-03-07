using System.ComponentModel.DataAnnotations;
#pragma warning disable CS8618 // Non-nullable field must contain a non-null value when exiting constructor. Consider adding the 'required' modifier or declaring as nullable.

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Request;

/// <summary>
/// Оплата по кредиту
/// </summary>
public class PaymentLoanRequestDto
{
    [Range(1, 10_000_000)]
    public int Amount { get; set; }
    
    [StringLength(20)]
    public string AccountNumber { get; set; }
}