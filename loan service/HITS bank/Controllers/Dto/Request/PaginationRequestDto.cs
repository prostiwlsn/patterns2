#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Common;

/// <summary>
/// Пагинация в запросе
/// </summary>
public class PaginationRequestDto
{
    public int PageSize { get; set; }
    
    public int PageNumber { get; set; }
}