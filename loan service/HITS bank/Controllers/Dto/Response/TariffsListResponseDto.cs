using HITS_bank.Controllers.Dto.Common;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Response;

/// <summary>
/// Список тарифов по кредитам
/// </summary>
public class TariffsListResponseDto
{
    public required List<TariffDto> Tariffs { get; set; }
    
    public required  PaginationResponseDto Pagination { get; set; }
}