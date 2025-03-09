#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Common;

/// <summary>
/// Пагинация ответа
/// </summary>
public class PaginationResponseDto
{
    public int PageSize { get; init; }
    
    public int PageNumber { get; init; }
    
    public int PagesCount { get; init; }
}