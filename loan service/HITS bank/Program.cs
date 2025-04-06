using System.Reflection;
using System.Text;
using System.Text.Json.Serialization;
using HITS_bank.Data;
using HITS_bank.Mappers;
using HITS_bank.Middlewares;
using HITS_bank.Repositories;
using HITS_bank.Services;
using HITS_bank.Services.BackgroundServices;
using HITS_bank.Services.Converter;
using HITS_bank.Services.Scheduler;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using Quartz;

var builder = WebApplication.CreateBuilder(args);
builder.Configuration.AddJsonFile("appsettings.json", optional: false, reloadOnChange: true);

var connection = builder.Configuration.GetConnectionString("DefaultConnection");

// Add services to the container.
builder.Services.AddDbContext<ApplicationDbContext>(options => options.UseNpgsql(connection));
AppContext.SetSwitch("Npgsql.EnableLegacyTimestampBehavior", true);

builder.Services.AddControllers().AddJsonOptions(x =>
{
    x.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter());
});

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddTransient<ExceptionsMiddleware>();

builder.Services.AddSwaggerGen(c =>
{
    c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        Name = "Authorization",
        Type = SecuritySchemeType.ApiKey,
        BearerFormat = "JWT",
        In = ParameterLocation.Header,
        Description = "Здесь введите JWT токен в формате: Bearer токен"
    });
    
    c.AddSecurityRequirement(new OpenApiSecurityRequirement
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
            new string[] { }
        }
    });
    
    var xmlFilename = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
    c.IncludeXmlComments(Path.Combine(AppContext.BaseDirectory, xmlFilename));
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

// Слушатели RabbitMq
builder.Services.AddSingleton<LoanPaymentConsumer>(sp =>
{
    var scopeFactory = sp.GetRequiredService<IServiceScopeFactory>();
    var config = sp.GetRequiredService<IConfiguration>();
    return new LoanPaymentConsumer(scopeFactory, config);
});

builder.Services.AddHostedService(sp => sp.GetRequiredService<LoanPaymentConsumer>());

// Планировщик задач
builder.Services.AddQuartz(q =>
{
    q.UseMicrosoftDependencyInjectionJobFactory();

    var jobKey = new JobKey("dailyPaymentJob");

    q.AddJob<PaymentSchedulerJob>(opts => opts.WithIdentity(jobKey));

    q.AddTrigger(opts => opts
        .ForJob(jobKey)
        .WithIdentity("dailyPaymentTrigger")
        .StartNow()
        .WithSimpleSchedule(x => x.WithIntervalInHours(24).RepeatForever())
    );
});
builder.Services.AddQuartzHostedService();

builder.Services.AddScoped<PaymentSchedulerJob>();
builder.Services.AddScoped<ISchedulerService, SchedulerService>();

// Мапперы
builder.Services.AddAutoMapper(typeof(LoanMapper));

// Сервисы
builder.Services.AddScoped<ILoanService, LoanService>();
builder.Services.AddScoped<ICurrencyConverter, CurrencyConverter>();
builder.Services.AddScoped<ISchedulerService, SchedulerService>();

// Репозитории
builder.Services.AddScoped<ILoanRepository, LoanRepository>();

var app = builder.Build();

// Обновление базы данных
using (var serviceScope = app.Services.CreateScope())
{
    var dbContext = serviceScope.ServiceProvider.GetRequiredService<ApplicationDbContext>();
    dbContext.Database.Migrate();
}

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthentication();

app.UseAuthorization();

app.UseMiddleware<ExceptionsMiddleware>();

app.MapControllers();

app.Run();