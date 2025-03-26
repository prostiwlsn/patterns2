using patterns_settings.Data;
using patterns_settings.Interfaces;
using patterns_settings.Models;

namespace patterns_settings.Services
{
    public class AdminSettingsService : IAdminSettingsService
    {
        private SettingsDbContext _dbContext;
        public AdminSettingsService(SettingsDbContext dbContext) 
        {
            _dbContext = dbContext;
        }

        public async Task EditSettings(Guid id, AdminClientSettingsDto model)
        {
            var settings = _dbContext.AdminClientSettings.Find(id);
            if (settings != null)
            {
                _dbContext.AdminClientSettings.Remove(settings);
            }

            AdminClientSettings newSettings = new AdminClientSettings
            {
                UserId = id,
                Theme = model.Theme,
            };

            _dbContext.Add(newSettings);
            _dbContext.SaveChanges();
        }

        public async Task<AdminClientSettingsDto> GetSettings(Guid id)
        {
            var settings = _dbContext.AdminClientSettings.Find(id);

            if (settings == null) throw new ArgumentException();

            return new AdminClientSettingsDto { Theme = settings.Theme };
        }
    }
}
