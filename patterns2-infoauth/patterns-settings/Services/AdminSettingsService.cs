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

        public Task EditSettings(Guid id, AdminClientSettingsDto model)
        {
            throw new NotImplementedException();
        }

        public Task<AdminClientSettingsDto> GetSettings(Guid id)
        {
            throw new NotImplementedException();
        }
    }
}
