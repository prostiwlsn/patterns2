using patterns_settings.Data;
using patterns_settings.Interfaces;
using patterns_settings.Models;

namespace patterns_settings.Services
{
    public class BankSettingsService : IBankSettingsService
    {
        private SettingsDbContext _dbContext;
        public BankSettingsService(SettingsDbContext dbContext) 
        {
            _dbContext = dbContext;
        }

        public Task EditSettings(Guid id, BankClientSettingsDto model)
        {
            throw new NotImplementedException();
        }

        public Task<BankClientSettingsDto> GetSettings(Guid id)
        {
            throw new NotImplementedException();
        }
    }
}
