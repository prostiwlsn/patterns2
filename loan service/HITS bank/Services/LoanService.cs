﻿using AutoMapper;
using HITS_bank.Controllers.Dto;
using HITS_bank.Controllers.Dto.Common;
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