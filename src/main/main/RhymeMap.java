package main;

import main.dictionaries.IDictionary;
import main.utils.RhymeUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since July 2018
 */
public class RhymeMap
{
    @Nonnull
    private final Map<UUID, Set<String>> m_uuidToRhymes;
    @Nonnull
    private final Map<String, UUID> m_stringToUUID;
    @Nonnull
    private final IDictionary m_dictionary;

    public RhymeMap(@Nonnull IDictionary dictionary)
    {
        m_dictionary = dictionary;
        m_stringToUUID = new ConcurrentHashMap<>();
        m_uuidToRhymes = new ConcurrentHashMap<>();
    }

    /**
     * Returns an empty set if no matches are found
     */
    @Nonnull
    public Set<String> getRhymes(@Nonnull String key)
    {
        if (!m_stringToUUID.containsKey(key.toUpperCase()))
        {
            _computeRhymes(key);
        }
        @CheckForNull UUID uuid = m_stringToUUID.get(key.toUpperCase());
        if (uuid != null)
        {
            return m_uuidToRhymes.get(uuid)
                .stream()
                .filter(r -> !r.equals(key))
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private void _computeRhymes(@Nonnull String key)
    {
        UUID uuid = UUID.randomUUID();
        Set<String> rhymes = m_dictionary.getWords()
            .parallelStream()
            .filter(p -> RhymeUtils.anyPronunciationsRhyme(key, p, m_dictionary))
            .collect(Collectors.toSet());

        for (String word : rhymes)
        {
            m_stringToUUID.put(word.toUpperCase(), uuid);
            m_uuidToRhymes.put(uuid, rhymes);
        }
    }
}
