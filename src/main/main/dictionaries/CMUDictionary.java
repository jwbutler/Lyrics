package main.dictionaries;

import com.google.common.collect.ImmutableList;
import main.utils.FileUtils;
import main.Logging;
import main.Pair;
import main.linguistics.Pronunciation;
import main.utils.StringUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
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
public class CMUDictionary implements IDictionary
{
    private static final String SPLIT_PATTERN = "  ";

    private final Map<String, List<Pronunciation>> m_words;

    public CMUDictionary()
    {
        try
        {
            Map<String, List<Pronunciation>> words = Files.lines(FileUtils.getPath("cmudict.0.7a"))
                .parallel()
                .map(CMUDictionary::_parseLine)
                .filter(p -> p != null && p.getSecond() != null)
                .collect(Collectors.groupingBy(Pair::getFirst, ConcurrentHashMap::new, Collectors.mapping(Pair::getSecond, Collectors.toList())));
            m_words = words;
        }
        catch (IOException e)
        {
            // Not much point continuing execution
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    public Set<String> getWords()
    {
        return m_words.keySet();
    }

    @Nonnull
    public List<Pronunciation> getPronunciations(@Nonnull String key)
    {
        return m_words.getOrDefault(key.toUpperCase(), Collections.emptyList());
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
                String key = StringUtils.alphabeticOnly(line.substring(0, index));

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
