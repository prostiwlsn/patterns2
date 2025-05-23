using Microsoft.Extensions.Caching.Memory;

namespace HITS_bank.Middlewares;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class IdempotencyMiddleware
{
    private readonly RequestDelegate _next;
    private readonly IMemoryCache _cache;

    public IdempotencyMiddleware(RequestDelegate next, IMemoryCache cache)
    {
        _next = next;
        _cache = cache;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        var idempotencyKey = context.Request.Headers["X-Idempotency-Key"].FirstOrDefault();

        if (!string.IsNullOrEmpty(idempotencyKey))
        {
            if (_cache.TryGetValue(idempotencyKey, out string cachedResponse))
            {
                context.Response.ContentType = "application/json";
                context.Response.Headers["X-SNEED"] = "SNEED";
                await context.Response.WriteAsync(cachedResponse);
                return;
            }

            var originalBodyStream = context.Response.Body;

            using var responseBody = new MemoryStream();
            context.Response.Body = responseBody;
            context.Response.Headers["X-SNEED"] = "CHUCK";

            await _next(context);

            responseBody.Seek(0, SeekOrigin.Begin);
            var responseText = await new StreamReader(responseBody).ReadToEndAsync();

            _cache.Set(idempotencyKey, responseText, TimeSpan.FromMinutes(5));

            responseBody.Seek(0, SeekOrigin.Begin);
            await responseBody.CopyToAsync(originalBodyStream);

            context.Response.Body = originalBodyStream;
        }
        else
        {
            await _next(context);
        }
    }
}