using AutoMapper;
using HITS_bank.Controllers.Dto;
using HITS_bank.Data.Entities;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Mappers;

public class LoanMapper : Profile
{
    public LoanMapper()
    {
        CreateMap<TariffDto, TariffEntity>();
    }
}