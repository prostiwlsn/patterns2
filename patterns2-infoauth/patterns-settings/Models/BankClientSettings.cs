using System.ComponentModel.DataAnnotations;

namespace patterns_settings.Models
{
    public class BankClientSettings
    {
        [Key]
        public Guid UserId { get; set; }
        public bool IsDark { get; set; }
        public List<Guid> HiddenAccounts { get; set; } = [];
    }
}
