using Microsoft.AspNetCore.Mvc;
using System.Collections.Concurrent;
using UserManagementAPI.Models;

namespace UserManagementAPI.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UsersController : ControllerBase
    {
        // Using ConcurrentDictionary to simulate a high-performance, thread-safe database
        private static readonly ConcurrentDictionary<int, User> _users = new();
        private static int _nextId = 1;

        // GET: api/users
        [HttpGet]
        public IActionResult GetAllUsers()
        {
            try
            {
                return Ok(_users.Values);
            }
            catch (Exception ex)
            {
                // Unhandled exceptions will also be caught by the global middleware
                throw new ApplicationException("Error retrieving users.", ex);
            }
        }

        // GET: api/users/{id}
        [HttpGet("{id}")]
        public IActionResult GetUserById(int id)
        {
            if (_users.TryGetValue(id, out var user))
            {
                return Ok(user);
            }
            return NotFound(new { message = $"User with ID {id} not found." });
        }

        // POST: api/users
        [HttpPost]
        public IActionResult AddUser([FromBody] User user)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            user.Id = Interlocked.Increment(ref _nextId);
            _users.TryAdd(user.Id, user);
            
            return CreatedAtAction(nameof(GetUserById), new { id = user.Id }, user);
        }

        // PUT: api/users/{id}
        [HttpPut("{id}")]
        public IActionResult UpdateUser(int id, [FromBody] User updatedUser)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (_users.TryGetValue(id, out var existingUser))
            {
                existingUser.Name = updatedUser.Name;
                existingUser.Email = updatedUser.Email;
                return Ok(existingUser);
            }
            return NotFound(new { message = $"User with ID {id} not found." });
        }

        // DELETE: api/users/{id}
        [HttpDelete("{id}")]
        public IActionResult DeleteUser(int id)
        {
            if (_users.TryRemove(id, out _))
            {
                return NoContent();
            }
            return NotFound(new { message = $"User with ID {id} not found." });
        }
    }
}
