using System.ComponentModel.DataAnnotations;

namespace patterns2_infoauth.Model
{
    public class UserCreationDto
    {
        [Phone]
        public string Phone { get; set; }
        [Length(8, 32)]
        public string Password { get; set; }
        public string Name { get; set; }
        public RoleType? Role { get; set; }
    }
}
