using System.ComponentModel.DataAnnotations;

namespace patterns_settings.Models
{
    public class AdminClientSettings
    {
        [Key]
        public Guid UserId { get; set; }
        public Themes Theme { get; set; }
    }
}
