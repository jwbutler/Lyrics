package lyrics.utils;

import lyrics.linguistics.Syllable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since July 2018
 */
public class MeterUtils
{
    private MeterUtils()
    {
    }

    /**
     * The only criterion here is that if the meter calls for an unstressed syllable,
     * the corresponding syllable in the line must also be unstressed.
     */
    public static boolean fitsMeter(@Nonnull List<Integer> targetMeter, @Nonnull List<Integer> lineMeter)
    {
        if (lineMeter.size() != targetMeter.size())
        {
            return false;
        }

        for (int i = 0; i < targetMeter.size(); i++)
        {
            // Don't allow a strong syllable in a weak position
            if (lineMeter.get(i) > 0 && targetMeter.get(i) == 0)
            {
                return false;
            }
            // Additionally, don't allow a weak syllable in a strong position at the end of the line
            if (i == targetMeter.size() - 1)
            {
                if (lineMeter.get(i) == 0 && targetMeter.get(i) > 0)
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Nonnull
    public static List<Integer> getMeterForSyllables(@Nonnull List<Syllable> syllables)
    {
        return syllables.stream()
            .map(Syllable::getEmphasis)
            .collect(Collectors.toList());
    }
}
