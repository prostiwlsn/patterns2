using System.ComponentModel.DataAnnotations;
using System.Security.Claims;
using HITS_bank.Controllers.Dto.Request;
using HITS_bank.Controllers.Dto.Response;
using HITS_bank.Services;
using HITS_bank.Utils;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using IResult = HITS_bank.Utils.IResult;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Controllers;

/// <summary>
/// Кредиты
/// </summary>
[ApiController]
[Route("api/loan")]
public class LoanController : ControllerBase
{
    private readonly ILoanService _loanService;

    public LoanController(ILoanService loanService)
    {
        _loanService = loanService;
    }
    
    /// <summary>
    /// Создание тарифа для кредита
    /// </summary>
    [HttpPost]
    [Route("tariff")]
    [Authorize]
    public async Task<IActionResult> CreateLoanTariff(CreateTariffRequestDto createTariffRequest)
    {
        await _loanService.CreateTariff(createTariffRequest);
        
        return Ok();
    }

    /// <summary>
    /// Получение списка тарифов
    /// </summary>
    [HttpGet]
    [Route("tariff")]
    [Authorize]
    [ProducesResponseType(StatusCodes.Status200OK, Type = typeof(TariffsListResponseDto))]
    public async Task<IActionResult> GetLoanTariffsList([Required]int pageNumber = 1, [Required]int pageSize = 10) 
    {
        var tariffsList = await _loanService.GetTariffs(pageNumber, pageSize);

        return GetResponseResult<TariffsListResponseDto>(tariffsList);
    }

    /// <summary>
    /// Удаление тарифа кредита
    /// </summary>
    [HttpDelete]
    [Route("tariff")]
    [Authorize]
    public async Task<IActionResult> DeleteLoanTariff([Required]Guid tariffId)
    {
        var deletionResult = await _loanService.DeleteTariff(tariffId);

        return GetResponseResult<TariffsListResponseDto>(deletionResult);
    }

    /// <summary>
    /// Обновление тарифа
    /// </summary>
    [HttpPut]
    [Route("tariff")]
    [Authorize]
    public async Task<IActionResult> UpdateLoanTariff(UpdateTariffRequestDto updatedTariff, [Required]Guid tariffId)
    {
        var response = await _loanService.UpdateTariff(updatedTariff, tariffId);
        
        return GetResponseResult<TariffDto>(response);
    }

    /// <summary>
    /// Взятие кредита
    /// </summary>
    [HttpPost]
    [Authorize]
    public async Task<IActionResult> CreateLoan(CreateLoanRequestDto loanRequest)
    {
        var userId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
        
        if (userId == null || Guid.Parse(userId) != loanRequest.UserId) 
            return StatusCode(StatusCodes.Status403Forbidden, "Operation forbidden");
        
        var response = await _loanService.CreateLoan(loanRequest);
        
        return GetResponseResult<CreateLoanRequestDto>(response);
    }

    /// <summary>
    /// Получение кредитов пользователя
    /// </summary>
    [HttpGet]
    [Route("{userId}/list")]
    [Authorize]
    [ProducesResponseType(StatusCodes.Status200OK, Type = typeof(LoansListResponseDto))]
    public async Task<IActionResult> GetUsersLoansList(Guid userId, int pageNumber = 1, int pageSize = 10)
    {
        var response = await _loanService.GetUserLoansList(userId, pageNumber, pageSize);
        
        return GetResponseResult<LoansListResponseDto>(response);
    }

    /// <summary>
    /// Получение кредитного рейтинга
    /// </summary>
    [HttpGet]
    [Route("{userId}/creditRating")]
    [Authorize]
    [ProducesResponseType(StatusCodes.Status200OK, Type = typeof(CreditRatingDto))]
    public async Task<IActionResult> GetUserCreditRating(Guid userId)
    {
        var response = await _loanService.GetCreditRating(userId);

        return Ok(response);
    }
    
    /// <summary>
    /// Формирование ответа
    /// </summary>
    private IActionResult GetResponseResult<T>(IResult result)
    {
        if (result is Success<T> response)
            return Ok(response.Data);

        if (result is Success)
            return Ok();
        
        if (result is Error error)
            return StatusCode(error.StatusCode, error.Message);

        return StatusCode(StatusCodes.Status500InternalServerError, "Internal Server Error");
    }
}