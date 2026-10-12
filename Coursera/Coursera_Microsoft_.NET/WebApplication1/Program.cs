using Microsoft.OpenApi.Models;
using UserManagementAPI.Middleware;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();

// Configure Swagger to accept Authorization tokens
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "TechHive User Management API", Version = "v1" });
    c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        Description = "Enter 'Bearer {token}' (Default test token: TechHive-Secret-Token)",
        Name = "Authorization",
        In = ParameterLocation.Header,
        Type = SecuritySchemeType.ApiKey,
        Scheme = "Bearer"
    });
    c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference { Type = ReferenceType.SecurityScheme, Id = "Bearer" }
            },
            Array.Empty<string>()
        }
    });
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

// 1. Error-handling middleware first (Catches exceptions from anything below it)
app.UseMiddleware<ErrorHandlingMiddleware>();

// 2. Authentication middleware next (Rejects unauthorized requests before they hit controllers or get logged as successful)
app.UseMiddleware<AuthenticationMiddleware>();

// 3. Logging middleware last (Logs the method, path, and response status code of authenticated requests)
app.UseMiddleware<LoggingMiddleware>();

app.UseAuthorization();
app.MapControllers();

app.Run();
