using Microsoft.Extensions.Caching.Memory;

namespace patterns2_infoauth.Middleware
{
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
                if (_cache.TryGetValue(idempotencyKey, out var cachedResponse))
                {
                    await context.Response.WriteAsync(cachedResponse.ToString());
                    return;
                }

                var originalBodyStream = context.Response.Body;
                using var responseBody = new MemoryStream();

                await _next(context);

                context.Response.Body = responseBody;
                _cache.Set(idempotencyKey, await new StreamReader(responseBody).ReadToEndAsync());

                await responseBody.CopyToAsync(originalBodyStream);
            }
            else
            {
                await _next(context);
            }
        }
    }

}
