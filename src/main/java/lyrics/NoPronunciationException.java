package lyrics;

import javax.annotation.Nonnull;

public final class NoPronunciationException extends Exception
{
    public NoPronunciationException(@Nonnull String word)
    {
        super(word);
    }
}
