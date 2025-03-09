using HITS_bank.Controllers.Dto.Common;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Controllers.Dto.Response;

/// <summary>
/// Ответ списка кредитов
/// </summary>
public class LoansListResponseDto
{
    public required List<LoanDto> Loans { get; set; }
    
    public required PaginationResponseDto Pagination { get; set; }
}