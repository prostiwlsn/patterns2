using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Data.Entities;

/// <summary>
/// Сущность тарифа кредита
/// </summary>
public class TariffEntity
{
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public Guid Id { get; set; }
    
    [MaxLength(100)]
    public required string Name { get; set; }
    
    public required double RatePercent { get; set; }
    
    [MaxLength(100)]
    public string? Description { get; set; }
}