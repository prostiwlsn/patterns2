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
}