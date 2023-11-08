package lyrics.dictionaries;

import lyrics.linguistics.Pronunciation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * @author jbutler
 * @since July 2018
 */
public interface Dictionary
{
    /**
     * @return a set of words, all uppercase
     */
    @Nonnull
    Set<String> getWords();

    /**
     * @param word Must be upper-case
     */
    @Nonnull
    Set<Pronunciation> getPronunciations(@Nonnull String word);
}
