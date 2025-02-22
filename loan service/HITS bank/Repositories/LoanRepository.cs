using AutoMapper;
using HITS_bank.Controllers.Dto;
using HITS_bank.Data;
using HITS_bank.Data.Entities;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Repositories;

public class LoanRepository : ILoanRepository
{
    private readonly ApplicationDbContext _context;
    private readonly IMapper _mapper;

    public LoanRepository(ApplicationDbContext context, IMapper mapper)
    {
        _context = context; _mapper = mapper;
    }
    
    /// <summary>
    /// Создание тарифа кредита
    /// </summary>
    public async Task AddTariff(TariffDto tariff)
    { 
        //  var tariffEntity = _mapper.Map<TariffEntity>(tariff);
        TariffEntity tariffEntity = new TariffEntity
      {
          Name = tariff.Name,
          RatePercent = tariff.RatePercent,
          Description = tariff.Description,
      }; 
        
      _context.Tariffs.Add(tariffEntity);
      await _context.SaveChangesAsync();
    }
}