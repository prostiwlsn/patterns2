using EasyNetQ;
using Newtonsoft.Json;
using Microsoft.EntityFrameworkCore;
using patterns2_infoauth.Data;
using patterns2_infoauth.Handlers;
using Microsoft.AspNetCore.Authentication;
using patterns2_infoauth.Interfaces;
using patterns2_infoauth.Services;
using Microsoft.OpenApi.Models;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using patterns2_infoauth.Middleware;
using EasyNetQ.DI;
using Microsoft.Extensions.Hosting;
using System.Text.Json.Serialization;
using Quartz;
using patterns2_infoauth.CronJobs;
using System.Net;
using Serilog;
using Serilog.Sinks.Grafana.Loki;
using System.Diagnostics;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

var levelSwitch = new Serilog.Core.LoggingLevelSwitch(Serilog.Events.LogEventLevel.Information);

Log.Logger = new LoggerConfiguration()
    .MinimumLevel.ControlledBy(levelSwitch)
    .Enrich.FromLogContext()
    .Enrich.WithEnvironmentName()
    .WriteTo.Console()
    .WriteTo.GrafanaLoki("http://194.59.186.122:3100", new List<LokiLabel> () { new LokiLabel { Key="app", Value="Auth"} })
    .CreateLogger();

builder.Host.UseSerilog();

builder.Services.AddControllersWithViews().AddJsonOptions(opts =>
{
    var enumConverter = new JsonStringEnumConverter();
    opts.JsonSerializerOptions.Converters.Add(enumConverter);
});
builder.Services.AddRazorPages();

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddDbContext<AuthDbContext>(options => options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

//builder.Services.AddSingleton<IBus>(RabbitHutch.CreateBus(builder.Configuration.GetConnectionString("BusConnection")));

builder.Services.AddScoped<IAuthService, AuthService>();
builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<IRoleService, RoleService>();
builder.Services.AddScoped<AuthMessageHandler>();

var rabbitMqConnectionString = builder.Configuration.GetConnectionString("BusConnection");
builder.Services.AddSingleton(new RpcServer(builder.Services.BuildServiceProvider(), rabbitMqConnectionString));

builder.Services.AddSwaggerGen(option =>
{
    //option.SwaggerDoc("v1", new OpenApiInfo { Title = "Demo API", Version = "v1" });
    option.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        In = ParameterLocation.Header,
        Description = "Please enter a valid token",
        Name = "Authorization",
        Type = SecuritySchemeType.Http,
        BearerFormat = "JWT",
        Scheme = "Bearer"
    });
    option.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference
                {
                    Type = ReferenceType.SecurityScheme,
                    Id = "Bearer"
                }
            },
            new string[]{}
        }
    });
});

builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer(o =>
{
    o.TokenValidationParameters = new TokenValidationParameters
    {
        ValidIssuer = builder.Configuration["Jwt:Issuer"],
        ValidAudience = builder.Configuration["Jwt:Audience"],
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(builder.Configuration["Jwt:Key"])),
        ValidateIssuer = true,
        ValidateAudience = false,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true
    };
});

builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("IsBlocked", policy =>
        policy.RequireClaim("isBlocked", "False"));

    options.AddPolicy("IsModerator", policy =>
        policy.RequireClaim("Role", "Moderator", "Admin"));

    options.AddPolicy("IsAdmin", policy =>
        policy.RequireClaim("Role", "Admin"));
});

builder.Services.AddQuartz(q =>
{
    var jobKey = new JobKey("SessionCleanupJob");
    q.AddJob<SessionCleanupJob>(opts => opts.WithIdentity(jobKey));

    q.AddTrigger(opts => opts
        .ForJob(jobKey)
        .WithIdentity("SessionCleanupTrigger")
        .WithCronSchedule("0 0 * * * ?"));
});

builder.Services.AddQuartzHostedService(q => q.WaitForJobsToComplete = true);

var app = builder.Build();

//await app.Services.GetService<IBus>().SetupListeners(app);

var rpcServer = app.Services.GetRequiredService<RpcServer>();
rpcServer.Start();

using (var serviceScope = app.Services.CreateScope())
{
    var dbContext = serviceScope.ServiceProvider.GetRequiredService<AuthDbContext>();
    dbContext.Database.Migrate();
}

// Configure the HTTP request pipeline.
app.UseSwagger();
app.UseSwaggerUI();

app.UseMiddleware<ExceptionHandlingMiddleware>();
app.UseMiddleware<RequestLoggingMiddleware>();
app.UseMiddleware<UnstableMiddleware>();
app.UseMiddleware<IdempotencyMiddleware>();

//app.Use(async (context, next) =>
//{
//    var sw = Stopwatch.StartNew();
//    try
//    {
//        await next();
//    }
//    finally
//    {
//        sw.Stop();
//        Log.ForContext("ResponseTimeMs", sw.ElapsedMilliseconds)
//           .ForContext("RequestPath", context.Request.Path)
//           .Information("Request completed");
//    }
//});

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
