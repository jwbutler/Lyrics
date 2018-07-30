package main.dictionaries;

import main.linguistics.Pronunciation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * @author jbutler
 * @since July 2018
 */
public interface IDictionary {
  @Nonnull List<String> getWords();
  @Nonnull List<Pronunciation> getPronunciations(@Nonnull String word);
  @Nonnull Set<String> getRhymes(@Nonnull String key);
}
