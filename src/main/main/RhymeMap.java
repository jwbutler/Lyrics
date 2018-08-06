package main;

import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.utils.RhymeUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
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
    private final Map<String, Set<String>> m_stringToRhymes;
    @Nonnull
    private final IDictionary m_dictionary;
    @Nonnull
    private final Map<List<Integer>, Map<String, List<Line>>> m_linesByMeter;

    public RhymeMap(@Nonnull IDictionary dictionary)
    {
        m_dictionary = dictionary;
        m_stringToRhymes = new ConcurrentHashMap<>();
        m_linesByMeter = new ConcurrentHashMap<>();
    }

    /**
     *
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
     */
    private void _computeRhymes(@Nonnull String key)
    {
        Set<String> rhymes = m_dictionary.getWords()
            .parallelStream()
            .filter(p -> RhymeUtils.anyPronunciationsRhyme(key, p, m_dictionary))
            .collect(Collectors.toSet());
        rhymes.forEach(rhymingWord -> m_stringToRhymes.put(rhymingWord, rhymes));
    }
}
