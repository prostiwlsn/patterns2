#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Utils;

/// <summary>
/// Результат выполнения запроса
/// </summary>
public interface IResult
{ }

/// <summary>
/// Успешный результат выполнения запроса с возвращаемым ответом
/// </summary>
public class Success<T> : IResult
{
    public Success(T? data)
    {
        Data = data;
    }
    
    public T? Data { get; }
}

/// <summary>
/// Успешный результат выполнения запроса
/// </summary>
public class Success : IResult { }

/// <summary>
/// Неуспешный результат выполнения запроса
/// </summary>
public class Error : IResult
{
    public Error(int statusCode, string message)
    {
        StatusCode = statusCode;
        Message = message;
    }
    
    public int StatusCode { get; }
    
    public string Message { get; }
}