using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using patterns_settings.Interfaces;
using patterns_settings.Models;
using System.Security.Claims;

namespace patterns_settings.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SettingsController : ControllerBase
    {
        private IBankSettingsService _bankSettingsService;
        private IAdminSettingsService _adminSettingsService;
        public SettingsController(IBankSettingsService bankSettingsService, IAdminSettingsService adminSettingsService)
        {
            _bankSettingsService = bankSettingsService;
            _adminSettingsService = adminSettingsService;
        }

        [HttpGet("admin/settings")]
        [Authorize]
        public async Task<IActionResult> GetAdminSettings()
        {
            var id = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (id == null)
                return Unauthorized("no user id");

            try
            {
                var settings = await _adminSettingsService.GetSettings(Guid.Parse(id));
                return Ok(settings);
            }
            catch (ArgumentException ex) 
            {
                return NotFound();
            }
            catch
            {
                return StatusCode(500);
            }
        }
        [HttpPut("admin/settings")]
        [Authorize]
        public async Task<IActionResult> PutAdminSettings(AdminClientSettingsDto model)
        {
            var id = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (id == null)
                return Unauthorized("no user id");

            try
            {
                await _adminSettingsService.EditSettings(Guid.Parse(id), model);
                return Ok();
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
            catch
            {
                return StatusCode(500);
            }
        }

        [HttpGet("bank/settings")]
        [Authorize]
        public async Task<IActionResult> GetBankSettings()
        {
            var id = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (id == null)
                return Unauthorized("no user id");

            try
            {
                var settings = await _bankSettingsService.GetSettings(Guid.Parse(id));
                return Ok(settings);
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
            catch
            {
                return StatusCode(500);
            }
        }
        [HttpPut("bank/settings")]
        [Authorize]
        public async Task<IActionResult> PutBankSettings(BankClientSettingsDto model)
        {
            var id = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (id == null)
                return Unauthorized("no user id");

            try
            {
                await _bankSettingsService.EditSettings(Guid.Parse(id), model);
                return Ok();
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
            catch
            {
                return StatusCode(500);
            }
        }
    }
}
