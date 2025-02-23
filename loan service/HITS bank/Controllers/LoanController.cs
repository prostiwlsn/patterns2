using System.ComponentModel.DataAnnotations;
using HITS_bank.Controllers.Dto;
using HITS_bank.Controllers.Dto.Common;
using HITS_bank.Controllers.Dto.Response;
using HITS_bank.Services;
using HITS_bank.Utils;
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
    [ProducesResponseType(StatusCodes.Status200OK, Type = typeof(TariffsListResponseDto))]
    public async Task<IActionResult> GetLoanTariffsList(
        [FromQuery][Required]int pageNumber, [FromQuery][Required]int pageSize) 
    {
        var tariffsList = await _loanService.GetTariffs(pageNumber, pageSize);

        return GetResponseResult(tariffsList);
    }

    /// <summary>
    /// Формирование ответа
    /// </summary>
    private IActionResult GetResponseResult(IResult result)
    {
        if (result is Success<TariffsListResponseDto> response)
        {
            return Ok(response.Data);
        }
        if (result is Error error)
        {
            return StatusCode(error.StatusCode, error.Message);
        }

        return StatusCode(500, "Internal server error");
    }
}