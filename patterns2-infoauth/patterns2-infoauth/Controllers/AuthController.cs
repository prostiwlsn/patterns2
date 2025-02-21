using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Model;

namespace patterns2_infoauth.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private IAuthService _authService;
        public AuthController(IAuthService authService)
        {
            _authService = authService;
        }

        [HttpPost("/register")]
        public async Task<IActionResult> Register([FromBody] UserCredentialsDto model)
        {
            try
            {
                string token = await _authService.Register(model);
                return Ok(token);
            }
            catch (ArgumentException argEx)
            {
                return BadRequest("This user already exists");
            }
        }

        [HttpPost("/login")]
        public async Task<IActionResult> Login([FromBody] LoginDto model)
        {
            try
            {
                string token = await _authService.Login(model);
                return Ok(token);
            }
            catch (ArgumentException argEx)
            {
                return BadRequest("This user doesn't exist");
            }
        }
    }
}
