using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace patterns_settings.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SettingsController : ControllerBase
    {
        [HttpGet]
        [Authorize]
        public async Task<IActionResult> GetAdminSettings()
        {
            return Ok();
        }
        [HttpPut]
        [Authorize]
        public async Task<IActionResult> PutAdminSettings()
        {
            return Ok();
        }

        [HttpGet]
        [Authorize]
        public async Task<IActionResult> GetBankSettings()
        {
            return Ok();
        }
        [HttpPut]
        [Authorize]
        public async Task<IActionResult> PutBankSettings()
        {
            return Ok();
        }
    }
}
