#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Response;

/// <summary>
/// Тариф кредита
/// </summary>
public class TariffDto
{
    public Guid Id { get; set; }

    public required string Name { get; set; }
    
    public double RatePercent { get; set; }
    
    public string? Description { get; set; }
}