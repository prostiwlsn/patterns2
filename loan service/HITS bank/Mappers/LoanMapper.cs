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

        CreateMap<CreateLoanRequestDto, LoanEntity>()
            .ForMember(dest => dest.IssueDate, opt => opt.MapFrom(src => DateTime.Now))
            .ForMember(dest => dest.EndDate, opt => opt.MapFrom(src => DateTime.Now.AddYears(src.DurationInYears)))
            .ForMember(dest => dest.Debt, opt => opt.MapFrom(src => src.Amount));

        CreateMap<LoanEntity, LoanDto>();
    }
}