package lyrics.linguistics;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Corresponds to stress indicators in the CMU Pronouncing Dictionary
 * http://www.speech.cs.cmu.edu/cgi-bin/cmudict
 *
 * @author jwbutler
 * @since November 2019
 */
public enum Emphasis
{
    NO_STRESS(0),
    PRIMARY_STRESS(1),
    SECONDARY_STRESS(2)
    ;

    private final int m_value;

    Emphasis(int value)
    {
        m_value = value;
    }

    @Nonnull
    public static Emphasis fromValue(int value)
    {
        return Stream.of(Emphasis.values())
            .filter(e -> e.m_value == value)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid emphasis: " + value));
    }
}
