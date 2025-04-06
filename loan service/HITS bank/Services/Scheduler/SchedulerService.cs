using System.ComponentModel;
using AutoMapper;
using HITS_bank.Controllers.Dto.Message;
using HITS_bank.Data.Entities;
using HITS_bank.Repositories;
using HITS_bank.Services.Converter;

namespace HITS_bank.Services.Scheduler;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class SchedulerService : ISchedulerService
{
    private readonly ILoanRepository _loanRepository;

    public SchedulerService(ILoanRepository loanRepository)
    {
        _loanRepository = loanRepository;
    }

    public async Task<List<DailyPaymentMessage>> GetDailyPaymentMessages()
    {
        await _loanRepository.IncreaseDebt();
        var loans = await _loanRepository.GetLoansList();

        var paymentMessages = new List<DailyPaymentMessage>();

        foreach (var loanEntity in loans)
        {
            var paymentAmount = loanEntity.Debt / (Math.Max((loanEntity.EndDate - DateTime.Now).Days, 0) + 1);

            if (loanEntity.AccountId != null)
            {
                paymentMessages.Add(new DailyPaymentMessage
                {
                    AccountId = loanEntity.AccountId ?? Guid.Empty,
                    Amount = paymentAmount,
                });
            }
        }
        
        return paymentMessages;
    }
}