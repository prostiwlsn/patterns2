using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;

namespace patterns2_infoauth.Model
{
    [PrimaryKey(nameof(Role), nameof(UserCredentialsId))]
    public class UserRole
    {
        [ForeignKey("UserCredentials")]
        public Guid UserCredentialsId { get; set; }
        public RoleType Role {  get; set; }
        public UserCredentials UserCredentials { get; set; }
    }

    public enum RoleType
    {
        Manager, 
        Admin
    }
}
