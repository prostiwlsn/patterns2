using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace patterns2_infoauth.Model
{
    public class UserInfo
    {
        [Key, ForeignKey("UserCredentials")]
        public Guid UserCredentialsId { get; set; }
        public string Name { get; set; }

        public UserCredentials UserCredentials { get; set; }
    }
}
