package lyrics;

import javax.annotation.Nonnull;

/**
 * @author jbutler
 * @since July 2018
 */
public record Pair<S, T>(@Nonnull S first, @Nonnull T second)
{
    /**
     * This is preferable to the basic constructor because it does type inference for you
     */
    @Nonnull
    public static <A, B> Pair<A, B> of(@Nonnull A first, @Nonnull B second)
    {
        return new Pair<>(first, second);
    }
}
