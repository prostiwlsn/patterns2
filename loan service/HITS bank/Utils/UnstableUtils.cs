namespace HITS_bank.Utils;
// ReSharper disable PossibleLossOfFraction
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class UnstableUtils
{
    private static int _successfulCount = 0;
    private static int _failedCount = 0;

    public static void CountResult(bool isSuccess)
    {
        if (isSuccess)
            ++_successfulCount;
        else 
            ++_failedCount;
    }

    public static bool CanRetry()
    {
        return _successfulCount / (_failedCount + _successfulCount) <= 0.7f;
    }

    public static bool IsRandomError()
    {
        var random = new Random().Next(1, int.MaxValue);
        var currentMinute = DateTime.Now.Minute;

        if (currentMinute % 2 == 0)
        {
            if (random % 100 <= 90)
                return true;
        }
        else
        {
            if (random % 2 == 0)
                return true;
        }

        return false;
    }
}