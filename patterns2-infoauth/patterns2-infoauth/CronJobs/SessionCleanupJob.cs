using patterns2_infoauth.Data;
using Quartz;

namespace patterns2_infoauth.CronJobs
{
    public class SessionCleanupJob : IJob
    {
        private readonly IServiceScopeFactory _serviceScopeFactory;
        public SessionCleanupJob(IServiceScopeFactory serviceScopeFactory)
        {
            _serviceScopeFactory = serviceScopeFactory;
        }
        public async Task Execute(IJobExecutionContext context)
        {
            using (var scope = _serviceScopeFactory.CreateScope())
            {
                var dbContext = scope.ServiceProvider.GetRequiredService<AuthDbContext>();

                var now = DateTime.UtcNow;
                var expiredSessions = dbContext.Sessions.Where(s => s.Expires < now);

                dbContext.Sessions.RemoveRange(expiredSessions);
                await dbContext.SaveChangesAsync();
            }
        }
    }
}
