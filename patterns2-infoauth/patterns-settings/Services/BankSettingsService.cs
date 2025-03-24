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

        public async Task EditSettings(Guid id, BankClientSettingsDto model)
        {
            var settings = _dbContext.BankClientSettings.Find(id);
            if (settings != null)
            {
                _dbContext.BankClientSettings.Remove(settings);
            }

            BankClientSettings newSettings = new BankClientSettings
            {
                UserId = id,
                Theme = model.Theme,
            };

            _dbContext.Add(newSettings);
            _dbContext.SaveChanges();
        }

        public async Task<BankClientSettingsDto> GetSettings(Guid id)
        {
            var settings = _dbContext.BankClientSettings.Find(id);

            if (settings == null) throw new ArgumentException();

            return new BankClientSettingsDto { Theme = settings.Theme };
        }
    }
}
