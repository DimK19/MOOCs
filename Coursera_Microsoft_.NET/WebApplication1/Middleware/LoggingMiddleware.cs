namespace UserManagementAPI.Middleware
{
    public class LoggingMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly ILogger<LoggingMiddleware> _logger;

        public LoggingMiddleware(RequestDelegate next, ILogger<LoggingMiddleware> logger)
        {
            _next = next;
            _logger = logger;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            var method = context.Request.Method;
            var path = context.Request.Path;

            // Log the incoming request
            _logger.LogInformation($"Incoming Request: {method} {path}");

            await _next(context);

            // Log the outgoing response
            _logger.LogInformation($"Outgoing Response: {method} {path} - Status: {context.Response.StatusCode}");
        }
    }
}
