using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Model;
using patterns2_infoauth.Services;

namespace patterns2_infoauth.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RoleController : ControllerBase
    {
        private IRoleService _roleService;
        public RoleController(IRoleService roleService)
        {
            _roleService = roleService;
        }

        [Authorize(Policy = "IsModerator")]
        [HttpPost("/role/{userId}/{role}")]
        public async Task<IActionResult> AddRole(Guid userId, RoleType role)
        {
            try
            {
                await _roleService.AddRole(userId, role);
                return Ok();
            }
            catch (ArgumentException ex)
            {
                return NotFound("User not found");
            }
            catch (DbUpdateException ex)
            {
                return BadRequest("This role already exists");
            }
        }
        [Authorize(Policy = "IsModerator")]
        [HttpDelete("/role/{userId}/{role}")]
        public async Task<IActionResult> RemoveRole(Guid userId, RoleType role)
        {
            try
            {
                await _roleService.RemoveRole(userId, role);
                return Ok();
            }
            catch (ArgumentException ex)
            {
                return NotFound("User not found");
            }
        }
    }
}
