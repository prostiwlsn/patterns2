using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace patterns2_infoauth.Model
{
    [PrimaryKey("Id")]
    public class UserCredentials
    {
        public Guid Id { get; set; }
        [Phone]
        public string Phone {  get; set; }
        [Length(8,32)]
        public string Password { get; set; }
        public string Name { get; set; }
        public bool IsBlocked { get; set; } = false;
        public List<UserRole> UserRoles { get; set; } = new List<UserRole>();
    }
}
