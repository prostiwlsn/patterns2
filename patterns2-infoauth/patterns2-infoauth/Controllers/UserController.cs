﻿using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Model;
using System.Numerics;
using System.Security.Claims;
using System.Xml.Linq;

namespace patterns2_infoauth.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private IUserService _userService;
        public UserController(IUserService userService)
        {
            _userService = userService;
        }

        [Authorize(Policy = "IsModerator")]
        [HttpGet("/users/{id}")]
        public async Task<IActionResult> GetUser(Guid id)
        {
            try
            {
                var user = await _userService.GetUser(id);
                return Ok(user);
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
        }

        [Authorize(Policy = "IsModerator")]
        [HttpGet("/users")]
        public async Task<IActionResult> GetUsers([FromQuery] string name = "", [FromQuery] string phone = "")
        {
            try
            {
                var users = await _userService.GetUsers(name, phone);
                return Ok(users);
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
        }

        [Authorize(Policy = "IsModerator")]
        [HttpPost("/users")]
        public async Task<IActionResult> CreateUser(UserCreationDto model)
        {
            await _userService.CreateUser(model);
            return Ok();
        }

        [Authorize(Policy = "IsModerator")]
        [HttpPut("/users/{id}")]
        public async Task<IActionResult> EditUser(Guid id,UserEditDto model)
        {
            try
            {
                await _userService.EditUser(model, id);
                return Ok();
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
        }

        [Authorize(Policy = "IsModerator")]
        [HttpPost("/users/{id}/block")]
        public async Task<IActionResult> BlockUser(Guid id)
        {
            try
            {
                await _userService.BlockUser(id);
                return Ok();
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
        }

        [Authorize(Policy = "IsModerator")]
        [HttpPost("/users/{id}/unblock")]
        public async Task<IActionResult> UnblockUser(Guid id)
        {
            try
            {
                await _userService.UnblockUser(id);
                return Ok();
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
        }

        [HttpGet("/profile")]
        [Authorize(Policy = "IsBlocked")  ]
        public async Task<IActionResult> GetProfile()
        {
            var id = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if(id == null)
                return BadRequest("no user id");

            try
            {
                var user = await _userService.GetUser(Guid.Parse(id));
                return Ok(user);
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
        }
        [HttpPut("/profile")]
        [Authorize(Policy = "IsBlocked")]
        public async Task<IActionResult> EditProfile(UserEditDto model)
        {
            var id = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (id == null)
                return BadRequest("no user id");

            try
            {
                await _userService.EditUser(model, Guid.Parse(id));
                return Ok();
            }
            catch (ArgumentException ex)
            {
                return NotFound();
            }
        }
    }
}
