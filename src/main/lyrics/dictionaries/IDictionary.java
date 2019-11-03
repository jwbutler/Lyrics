package lyrics.dictionaries;

import lyrics.linguistics.Pronunciation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * @author jbutler
 * @since July 2018
 */
public interface IDictionary {
  @Nonnull Set<String> getWords();
  @Nonnull List<Pronunciation> getPronunciations(@Nonnull String word);
}
