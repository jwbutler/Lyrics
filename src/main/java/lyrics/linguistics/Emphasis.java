package lyrics.linguistics;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * @author jwbutler
 * @since November 2019
 */
public enum Emphasis
{
    STRONG(1),
    WEAK(0)
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
            .orElseThrow(IllegalArgumentException::new);
    }
}
