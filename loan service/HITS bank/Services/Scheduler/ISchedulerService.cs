using HITS_bank.Controllers.Dto.Message;

namespace HITS_bank.Services.Scheduler;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public interface ISchedulerService
{
    public Task<List<DailyPaymentMessage>> GetDailyPaymentMessages();
}