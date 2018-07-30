package main.dictionaries;

import com.google.common.collect.ImmutableList;
import main.utils.FileUtils;
import main.Logging;
import main.Pair;
import main.linguistics.Pronunciation;
import main.utils.RhymeUtils;
import main.utils.StringUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since July 2018
 */
public class CMUDictionary implements IDictionary
{
    private static final String SPLIT_PATTERN = "  ";

    private final SortedMap<String, List<Pronunciation>> m_words;
    private final Map<String, Set<String>> m_rhymes;
    private final List<String> m_sortedKeys;

    public CMUDictionary() throws IOException
    {
        SortedMap<String, List<Pronunciation>> words = Files.lines(FileUtils.getPath("cmudict.0.7a"))
            .parallel()
            .map(CMUDictionary::_parseLine)
            .filter(p -> p != null && p.getSecond() != null)
            .collect(Collectors.groupingBy(
                Pair::getFirst,
                TreeMap::new,
                Collectors.mapping(
                    Pair::getSecond,
                    Collectors.toList()
                )
            ));
        m_words = words;
        m_rhymes = new HashMap<>();
        m_sortedKeys = ImmutableList.copyOf(words.keySet());
    }

    @Nonnull
    public List<String> getWords()
    {
        return m_sortedKeys;
    }

    @Nonnull
    public List<Pronunciation> getPronunciations(@Nonnull String key)
    {
        return m_words.getOrDefault(key.toUpperCase(), Collections.emptyList());
    }

    /**
     * Returns an empty set if no matches are found
     */
    @Nonnull
    public Set<String> getRhymes(@Nonnull String key)
    {
        return m_rhymes.computeIfAbsent(key.toUpperCase(), this::_getRhymes);
    }

    @Nonnull
    private Set<String> _getRhymes(@Nonnull String key)
    {
        return m_words.keySet()
            .parallelStream()
            .filter(p -> RhymeUtils.anyPronunciationsRhyme(key, p, this))
            .collect(Collectors.toSet());
    }

    /**
     * @return a pair of (key, list of phonemes)
     * or null if it's not a valid line
     */
    @CheckForNull
    private static Pair<String, Pronunciation> _parseLine(@Nonnull String line)
    {
        try
        {
            if (_isValidLine(line))
            {
                int index = line.indexOf(SPLIT_PATTERN);
                String key = StringUtils.sanitize(line.substring(0, index));

                return Pair.of(key, new Pronunciation(line.substring(index + 2, line.length())));
            }
            else
            {
                Logging.debug("Invalid line: " + line);
            }
        }
        catch (Exception e)
        {
            Logging.debug("Could not parse line: " + line, e);
        }
        return null;
    }

    /**
     * Cases we want to exclude:
     * - comments, which start with ";;;"
     * - symbols, followed by their pronunciation
     */
    private static boolean _isValidLine(@Nonnull String line)
    {
        return line.matches("^[a-zA-Z].*");
    }
}