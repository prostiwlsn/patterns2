using System.ComponentModel.DataAnnotations;

namespace patterns2_infoauth.Model
{
    public class LoginDto
    {
        [Phone]
        public string Phone { get; set; }
        [Length(8, 32)]
        public string Password { get; set; }
    }
}
