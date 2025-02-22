using System.ComponentModel.DataAnnotations;

namespace patterns2_infoauth.Model
{
    public class UserCredentialsDto
    {
        [Phone]
        public string Phone { get; set; }
        [Length(8, 32)]
        public string Password { get; set; }
        public string Name { get; set; }
    }
}
