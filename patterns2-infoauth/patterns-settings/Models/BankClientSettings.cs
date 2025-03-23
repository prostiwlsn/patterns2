namespace patterns_settings.Models
{
    public class BankClientSettings
    {
        public bool IsDark { get; set; }
        public List<Guid> HiddenAccounts { get; set; } = new List<Guid>();
    }
}
