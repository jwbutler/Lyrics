package lyrics.utils;

import java.util.Locale;
import javax.annotation.Nonnull;

/**
 * @author jbutler
 * @since July 2018
 */
public final class StringUtils
{
    private StringUtils() {}
    
    @Nonnull
    public static String alphanumericOnly(@Nonnull String input)
    {
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }

    @Nonnull
    public static String alphabeticOnly(@Nonnull String input)
    {
        return input.replaceAll("[^a-zA-Z]", "");
    }
    
    public static boolean isUpperCase(@Nonnull String input)
    {
        return input.equals(input.toUpperCase());
    }
}
