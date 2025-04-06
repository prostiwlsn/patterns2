using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using HITS_bank.Controllers.Dto.Message.AccountDto;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Request;

/// <summary>
/// Создание кредита
/// </summary>
public class CreateLoanRequestDto
{
    public Guid TariffId { get; set; }
    
    public Guid UserId { get; set; }
    
    public Guid AccountId { get; set; }
    
    [DefaultValue(CurrencyEnum.Usd)]
    public CurrencyEnum Currency { get; set; }
    
    [Range(1, 5)]
    public int DurationInYears { get; set; }
    
    [Range(1, 10_000_000)]
    public int Amount { get; set; }
}