package lyrics;

import lyrics.dictionaries.Dictionary;
import lyrics.utils.RhymeUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since July 2018
 */
public class RhymeMap
{
    @Nonnull
    private final Dictionary m_dictionary;
    @Nonnull
    private final Map<String, Set<String>> m_stringToRhymes;

    public RhymeMap(@Nonnull Dictionary dictionary)
    {
        m_dictionary = dictionary;
        m_stringToRhymes = new HashMap<>();
    }

    /**
     * Returns an empty set if no matches are found
     */
    @Nonnull
    public Set<String> getRhymes(@Nonnull String key)
    {
        if (!m_stringToRhymes.containsKey(key))
        {
            _computeRhymes(key);
        }
        return m_stringToRhymes.get(key.toUpperCase())
            .stream()
            .filter(word -> !word.equalsIgnoreCase(key))
            .collect(Collectors.toSet());
    }

    /**
     * Returns dictionary words, presumably all-caps.
     * Note that this *includes* the key, for storage reasons; we'll filter it out
     * on retrieval
     */
    private void _computeRhymes(@Nonnull String key)
    {
        Set<String> rhymes = m_dictionary.getWords()
            .stream()
            .filter(p -> RhymeUtils.anyPronunciationsRhyme(key, p, m_dictionary))
            .collect(Collectors.toSet());
        rhymes.forEach(rhymingWord -> m_stringToRhymes.put(rhymingWord, rhymes));
    }
}
