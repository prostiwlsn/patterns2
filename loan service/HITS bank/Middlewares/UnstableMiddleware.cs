namespace HITS_bank.Middlewares;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class UnstableMiddleware
{
    private readonly RequestDelegate _next;

    public UnstableMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        var random = new Random().Next(1, int.MaxValue);
        var currentMinute = DateTime.Now.Minute;

        if (currentMinute % 2 == 0)
        {
            if (random % 100 <= 90)
                throw new Exception();
        }
        else
        {
            if (random % 2 == 0)
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