namespace patterns2_infoauth.Middleware
{
    using System.Diagnostics;
    using System.Threading.Tasks;
    using Microsoft.AspNetCore.Http;
    using Serilog;
    using Serilog.Context;

    public class RequestLoggingMiddleware
    {
        private readonly RequestDelegate _next;
        public RequestLoggingMiddleware(RequestDelegate next) => _next = next;

        public async Task InvokeAsync(HttpContext context)
        {
            // 1. Начало обработки: трассировка
            Log.ForContext("RequestPath", context.Request.Path)
               .ForContext("RequestMethod", context.Request.Method)
               .Verbose("Started processing HTTP {RequestMethod} {RequestPath}",
                         context.Request.Method, context.Request.Path);

            var stopwatch = Stopwatch.StartNew();

            try
            {
                // 2. Передать дальше
                await _next(context);

                // 3. Успешное завершение: лог уровня Information с временем
                stopwatch.Stop();
                Log.ForContext("RequestPath", context.Request.Path)
                   .ForContext("RequestMethod", context.Request.Method)
                   .ForContext("StatusCode", context.Response.StatusCode)
                   .ForContext("ResponseTimeMs", stopwatch.ElapsedMilliseconds)
                   .Information("Completed HTTP {RequestMethod} {RequestPath} with {StatusCode} in {ResponseTimeMs} ms",
                                context.Request.Method, context.Request.Path,
                                context.Response.StatusCode, stopwatch.ElapsedMilliseconds);
            }
            catch (Exception ex)
            {
                // 4. Лог ошибки с трассировкой стека и временем до ошибки
                stopwatch.Stop();
                Log.ForContext("RequestPath", context.Request.Path)
                   .ForContext("RequestMethod", context.Request.Method)
                   .ForContext("ResponseTimeMs", stopwatch.ElapsedMilliseconds)
                   .Error(ex, "Error processing HTTP {RequestMethod} {RequestPath} after {ResponseTimeMs} ms",
                          context.Request.Method, context.Request.Path, stopwatch.ElapsedMilliseconds);
                throw; // обязательно пробросить, чтобы остальные middleware/фреймворк тоже обработали ошибку
            }
        }
    }
}
