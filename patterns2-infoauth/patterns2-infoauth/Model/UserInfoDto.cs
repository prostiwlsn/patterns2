using System.ComponentModel.DataAnnotations;

namespace patterns2_infoauth.Model
{
    public class UserInfoDto
    {
        public Guid Id { get; set; }
        public string Phone { get; set; }
        public string Name { get; set; }
        public bool IsBlocked { get; set; }
        public RoleType[] Roles { get; set; } 
    }
}
