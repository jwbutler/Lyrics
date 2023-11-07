package lyrics;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * @author jbutler
 * @since July 2018
 */
@Immutable
public final class Pair<S, T>
{
    private final S m_s;
    private final T m_t;

    private Pair(@Nonnull S s, @Nonnull T t)
    {
        m_s = s;
        m_t = t;
    }

    /**
     * This is preferable to the basic constructor because it does type inference for you
     */
    @Nonnull
    public static <A, B> Pair<A, B> of(@Nonnull A s, @Nonnull B t)
    {
        return new Pair<>(s, t);
    }

    @Nonnull
    public S getFirst()
    {
        return m_s;
    }

    @Nonnull
    public T getSecond()
    {
        return m_t;
    }
}
