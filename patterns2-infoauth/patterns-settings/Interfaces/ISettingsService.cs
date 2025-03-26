namespace patterns_settings.Interfaces
{
    public interface ISettingsService<T>
    {
        Task EditSettings(Guid id, T model);
        Task<T> GetSettings(Guid id);
    }
}
