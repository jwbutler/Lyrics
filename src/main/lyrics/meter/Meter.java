package lyrics.meter;

import com.google.common.annotations.VisibleForTesting;
import lyrics.linguistics.Emphasis;
import lyrics.linguistics.Syllable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jwbutler
 * @since November 2019
 */
public interface Meter
{
    int getNumSyllables();
    @Nonnull Emphasis getEmphasis(int syllableNumber);
    boolean fitsLineMeter(@Nonnull Meter lineMeter);
    boolean isEmpty();

    @Nonnull
    static Meter forSyllables(@Nonnull List<Syllable> syllables)
    {
        List<Emphasis> emphasisList = syllables.stream()
            .map(Syllable::getEmphasis)
            .collect(Collectors.toList());
        return new MeterImpl(emphasisList);
    }

    @Nonnull
    static Meter of(int... emphasisValues)
    {
        List<Emphasis> emphasisList = Arrays.stream(emphasisValues)
            .mapToObj(i -> i == 1 ? Emphasis.STRONG : Emphasis.WEAK)
            .collect(Collectors.toList());

        return new MeterImpl(emphasisList);
    }

    @Nonnull
    static Meter of(@Nonnull List<Integer> emphasisValues)
    {
        return Meter.of(emphasisValues.stream().mapToInt(Integer::intValue).toArray());
    }
}
