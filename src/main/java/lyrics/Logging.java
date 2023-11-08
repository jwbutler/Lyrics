package lyrics;

/**
 * @author jbutler
 * @since July 2018
 */
public final class Logging
{
    private static final boolean DEBUG_ENABLED = false;
    
    private Logging() {}

    public static void debug(String s)
    {
        if (DEBUG_ENABLED)
        {
            System.out.println(s);
        }
    }

    public static void debug(String s, Throwable e)
    {
        if (DEBUG_ENABLED)
        {
            System.out.println(s);
            e.printStackTrace();
        }
    }

    public static void debugF(String pattern, Object... args)
    {
        if (DEBUG_ENABLED)
        {
            System.out.printf(String.format(pattern + "\n", args));
        }
    }

    public static void info(String s)
    {
        System.out.println(s);
    }

    public static void info(String s, Throwable e)
    {
        System.out.println(s);
        e.printStackTrace();
    }

    public static void infoF(String pattern, Object... args)
    {
        System.out.printf(String.format(pattern + "\n", args));
    }

    public static void error(String s)
    {
        System.out.println(s);
    }

    public static void error(String s, Throwable e)
    {
        System.out.println(s);
        e.printStackTrace();
    }
}
