package lyrics;

import lyrics.dictionaries.Dictionary;
import lyrics.utils.RhymeUtils;
import lyrics.utils.StringUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static lyrics.utils.StringUtils.isUpperCase;

/**
 * @author jbutler
 * @since July 2018
 */
final class LazyLoadedRhymeMap implements RhymeMap
{
    @Nonnull
    private final Dictionary m_dictionary;
    @Nonnull
    private final Map<String, Set<String>> m_stringToRhymes;

    LazyLoadedRhymeMap(@Nonnull Dictionary dictionary)
    {
        m_dictionary = dictionary;
        m_stringToRhymes = new HashMap<>();
    }

    @Nonnull
    @Override
    public Set<String> getRhymes(@Nonnull String key)
    {
        assert isUpperCase(key);

        if (!m_stringToRhymes.containsKey(key))
        {
            _computeRhymes(key);
        }

        return m_stringToRhymes.get(key)
            .stream()
            .filter(word -> !word.equals(key))
            .collect(Collectors.toSet());
    }

    /**
     * Returns dictionary words, in all-caps.
     * Note that this *includes* the key, for storage reasons; we'll filter it out
     * on retrieval
     * 
     * @param key Must be uppercase
     */
    private void _computeRhymes(@Nonnull String key)
    {
        assert isUpperCase(key);

        Set<String> rhymes = m_dictionary.getWords()
            .stream()
            .filter(p -> RhymeUtils.anyPronunciationsRhyme(key, p, m_dictionary))
            .collect(Collectors.toSet());

        for (var rhymingWord : rhymes)
        {
            m_stringToRhymes.put(rhymingWord, rhymes);
        }
    }
}
