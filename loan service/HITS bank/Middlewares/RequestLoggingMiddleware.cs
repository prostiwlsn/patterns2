using System.Diagnostics;
using Serilog;

namespace HITS_bank.Middlewares;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class RequestLoggingMiddleware
{
    private readonly RequestDelegate _next;
    public RequestLoggingMiddleware(RequestDelegate next) => _next = next;

    public async Task InvokeAsync(HttpContext context)
    {
        var traceId = Activity.Current?.TraceId;
        
        Log.ForContext("TraceId", traceId)
            .ForContext("RequestPath", context.Request.Path)
            .ForContext("RequestMethod", context.Request.Method)
            .Verbose("Started processing HTTP {RequestMethod} {RequestPath}",
                context.Request.Method, context.Request.Path);

        var stopwatch = Stopwatch.StartNew();

        try
        {
            await _next(context);

            stopwatch.Stop();
            Log.ForContext("TraceId", traceId)
                .ForContext("RequestPath", context.Request.Path)
                .ForContext("RequestMethod", context.Request.Method)
                .ForContext("StatusCode", context.Response.StatusCode)
                .ForContext("ResponseTimeMs", stopwatch.ElapsedMilliseconds)
                .Information("Completed HTTP {RequestMethod} {RequestPath} with {StatusCode} in {ResponseTimeMs} ms",
                    context.Request.Method, context.Request.Path,
                    context.Response.StatusCode, stopwatch.ElapsedMilliseconds);
        }
        catch (Exception ex)
        {
            stopwatch.Stop();
            Log.ForContext("TraceId", traceId)
                .ForContext("RequestPath", context.Request.Path)
                .ForContext("RequestMethod", context.Request.Method)
                .ForContext("ResponseTimeMs", stopwatch.ElapsedMilliseconds)
                .Error(ex, "Error processing HTTP {RequestMethod} {RequestPath} after {ResponseTimeMs} ms",
                    context.Request.Method, context.Request.Path, stopwatch.ElapsedMilliseconds);
            throw;
        }
    }
}