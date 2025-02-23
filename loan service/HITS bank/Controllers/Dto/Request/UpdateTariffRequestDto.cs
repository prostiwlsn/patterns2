using System.ComponentModel.DataAnnotations;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Request;

/// <summary>
/// Изменение тарифа кредита
/// </summary>
public class UpdateTariffRequestDto
{
    [Required]
    [MaxLength(100)]
    public required string Name { get; set; }

    [Required]
    [Range(0, 100)]
    public double RatePercent { get; set; }

    [MaxLength(100)]
    public string? Description { get; set; }
}