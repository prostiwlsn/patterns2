using AutoMapper;
using HITS_bank.Controllers.Dto;
using HITS_bank.Controllers.Dto.Common;
using HITS_bank.Controllers.Dto.Request;
using HITS_bank.Controllers.Dto.Response;
using HITS_bank.Data.Entities;
using HITS_bank.Repositories;
using HITS_bank.Utils;
using IResult = HITS_bank.Utils.IResult;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Services;

/// <summary>
/// Сервис кредитов
/// </summary>
public class LoanService : ILoanService
{
    private readonly ILoanRepository _loanRepository;
    private readonly IMapper _mapper;

    public LoanService(ILoanRepository loanRepository, IMapper mapper)
    {
        _loanRepository = loanRepository;
        _mapper = mapper;
    }
    
    /// <summary>
    /// Создание тарифа кредита
    /// </summary>
    public async Task CreateTariff(CreateTariffRequestDto createTariffRequest)
    {
        var tariffEntity = _mapper.Map<TariffEntity>(createTariffRequest);
        await _loanRepository.AddTariff(tariffEntity);
    }

    /// <summary>
    /// Удаление тарифа кредита
    /// </summary>
    public async Task<IResult> DeleteTariff(Guid tariffId)
    {
        return await _loanRepository.DeleteTariff(tariffId);
    }

    /// <summary>
    /// Обновление тарифа
    /// </summary>
    public async Task<IResult> UpdateTariff(UpdateTariffRequestDto updatedTariff, Guid tariffId)
    {
        var updatedTariffEntity = _mapper.Map<TariffEntity>(updatedTariff);
        updatedTariffEntity.Id = tariffId;
        
        return await _loanRepository.UpdateTariff(updatedTariffEntity, tariffId);
    }

    /// <summary>
    /// Получение списка тарифов
    /// </summary>
    public async Task<IResult> GetTariffs(int pageNumber, int pageSize)
    {
        // Валидация
        var validationResult = ValidatePagination(pageNumber, pageSize);
        
        if (validationResult != null)
            return validationResult;
        
        // Получение списка тарифов
        var offset = (pageNumber - 1) * pageSize;
        var tariffsEntities = await _loanRepository.GetTariffs(offset: offset, limit: pageSize);
        
        // Получение пагинации
        var paginationResponseDto = new PaginationResponseDto
        {
            PageSize = pageSize,
            PageNumber = pageNumber,
            PagesCount = tariffsEntities.Count / pageSize + 1,
        };
        
        // Маппинг ответа
        var tariffsDtoList = _mapper.Map<List<TariffDto>>(tariffsEntities);
        var response = new TariffsListResponseDto
        {
            Tariffs = tariffsDtoList,
            Pagination = paginationResponseDto,
        };
        return new Success<TariffsListResponseDto>(response);
    }

    /// <summary>
    /// Создание кредита
    /// </summary>
    public async Task<IResult> CreateLoan(CreateLoanRequestDto createLoanRequest)
    {
        var selectedTariff = await _loanRepository.GetTariff(createLoanRequest.TariffId);
        
        if (selectedTariff == null)
            return new Error(StatusCodes.Status404NotFound, "Tariff not found");
        
        var loanEntity = _mapper.Map<LoanEntity>(createLoanRequest);
        loanEntity.RatePercent = selectedTariff.RatePercent;

        await _loanRepository.AddLoan(loanEntity);
        
        return new Success();
    }
    
    /// <summary>
    /// Валидация пагинации
    /// </summary>
    private static IResult? ValidatePagination(int pageNumber, int pageSize)
    {
        if (pageNumber < 1)
            return new Error(StatusCodes.Status400BadRequest, "Page number cannot be less than 1");
        
        if (pageSize < 0)
            return new Error(StatusCodes.Status400BadRequest, "Page size cannot be less than 0");
        
        return null;
    }
}