namespace patterns_settings.Models
{
    public class BankClientSettingsDto
    {
        public Themes Theme { get; set; }
        public List<Guid> HiddenAccounts { get; set; } = [];
    }
}
