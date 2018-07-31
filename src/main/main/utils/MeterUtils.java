package main.utils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public class MeterUtils
{
    private MeterUtils()
    {
    }

    public static boolean fitsMeter(@Nonnull List<Integer> targetMeter, @Nonnull List<Integer> lineMeter)
    {
        if (lineMeter.size() != targetMeter.size())
        {
            return false;
        }

        for (int i = 0; i < targetMeter.size(); i++)
        {
            if (lineMeter.get(i) == 1 && targetMeter.get(i) == 0)
            {
                return false;
            }
        }
        return true;
    }
}
