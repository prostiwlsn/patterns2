using System.Net;

namespace patterns2_infoauth.Middleware
{
    public class UnstableMiddleware
    {
        private readonly RequestDelegate _next;

        public UnstableMiddleware(RequestDelegate next)
        {
            _next = next;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            Random rnd = new Random();
            int result = rnd.Next(1, 100);

            if (result > 50)
            {
                throw new Exception();
            }

            try
            {
                await _next(context);
            }
            catch (Exception ex)
            {
                throw;
            }
        }
    }
}
