#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Response;

/// <summary>
/// Тариф кредита
/// </summary>
public class TariffDto
{
    public Guid Id { get; init; }

    public required string Name { get; init; }
    
    public double RatePercent { get; init; }
    
    public string? Description { get; init; }
}