package lyrics.utils;

public final class Preconditions
{
    private Preconditions() {}
    
    public static void checkArgument(boolean condition)
    {
        if (!condition)
        {
            throw new IllegalArgumentException();
        }
    }

    public static void checkState(boolean condition)
    {
        if (!condition)
        {
            throw new IllegalStateException();
        }
    }
}
