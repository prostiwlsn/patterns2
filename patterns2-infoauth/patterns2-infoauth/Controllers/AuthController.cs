using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Model;
using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;

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
        public async Task<IActionResult> Register(UserCredentialsDto model)
        {
            try
            {
                var token = await _authService.Register(model);
                return Ok(token);
            }
            catch (ArgumentException argEx)
            {
                Console.WriteLine(argEx.Message);
                return BadRequest("This user already exists");
            }
        }

        [HttpPost("/login")]
        public async Task<IActionResult> Login(LoginDto model)
        {
            try
            {
                var token = await _authService.Login(model);
                return Ok(token);
            }
            catch (ArgumentException argEx)
            {
                return BadRequest("This user doesn't exist");
            }
            catch (InvalidOperationException invEx)
            {
                return Forbid();
            }
        }

        [HttpPost("/refresh")]
        public async Task<IActionResult> Refresh()
        {
            var type = User.FindFirstValue("type");

            if (type == null)
                return BadRequest("no token type specified");

            if (type != "refresh")
                return BadRequest(new { Message = "Wrong token type" });

            var sessionIdString = User.FindFirstValue("sessionId");

            if (sessionIdString == null)
                return BadRequest("no session id");

            Guid sessionId = new Guid(sessionIdString);

            try
            {
                var tokens = await _authService.Refresh(sessionId);

                return Ok(tokens);
            }
            catch (ArgumentException ex)
            {
                return NotFound(ex.Message);
            }
            catch (InvalidOperationException invEx)
            {
                return Forbid();
            }
        }

        [HttpPost("/logout")]
        [Authorize]
        public async Task<IActionResult> Logout()
        {
            var sessionIdString = User.FindFirstValue("sessionId");

            //Console.WriteLine(JsonSerializer.Serialize(User.Claims.Select(c => new { c.Type, c.Value }).ToList()));

            if (sessionIdString == null)
                return BadRequest("no session id");

            Guid sessionId = new Guid(sessionIdString);

            try
            {
                await _authService.Logout(sessionId);
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }

            return Ok();
        }
    }
}
