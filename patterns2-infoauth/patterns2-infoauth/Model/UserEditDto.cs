using System.ComponentModel.DataAnnotations;

namespace patterns2_infoauth.Model
{
    public class UserEditDto
    {
        [Phone]
        public string Phone { get; set; }
        public string Name { get; set; }
    }
}
