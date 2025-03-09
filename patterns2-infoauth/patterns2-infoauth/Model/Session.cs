using System.ComponentModel.DataAnnotations;

namespace patterns2_infoauth.Model
{
    public class Session
    {
        [Key]
        public Guid Id { get; set; }
        public Guid UserId { get; set; }
        public bool IsActive { get; set; } = true;
        public DateTime Expires { get; set; }
    }
}
