using AutoMapper;
using HITS_bank.Controllers.Dto;
using HITS_bank.Controllers.Dto.Common;
using HITS_bank.Controllers.Dto.Request;
using HITS_bank.Controllers.Dto.Response;
using HITS_bank.Data.Entities;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Mappers;

public class LoanMapper : Profile
{
    public LoanMapper()
    {
        CreateMap<CreateTariffRequestDto, TariffEntity>();
        
        CreateMap<TariffEntity, TariffDto>();
        
        CreateMap<PaginationRequestDto, PaginationResponseDto>();
        
        CreateMap<PaginationResponseDto, TariffsListResponseDto>();
        
        CreateMap<UpdateTariffRequestDto, TariffEntity>();
    }
}