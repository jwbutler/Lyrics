package lyrics.dictionaries;

import lyrics.utils.FileUtils;
import lyrics.Logging;
import lyrics.Pair;
import lyrics.linguistics.Pronunciation;
import lyrics.utils.StringUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since July 2018
 */
public class CMUDictionary implements Dictionary
{
    private static final String SPLIT_PATTERN = "  ";

    private final Map<String, List<Pronunciation>> m_words;

    public CMUDictionary()
    {
        System.out.println("CMUDictionary - Loading...");
        long startTime = System.currentTimeMillis();
        m_words = FileUtils.getBufferedReader("cmudict.0.7a")
            .lines()
            .map(CMUDictionary::_parseLine)
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                Pair::first,
                Collectors.mapping(Pair::second, Collectors.toList())
            ));
        long endTime = System.currentTimeMillis();
        System.out.printf("CMUDictionary - Loaded %d words in %d ms\n", m_words.size(), (endTime - startTime));
    }

    @Nonnull
    @Override
    public Set<String> getWords()
    {
        return m_words.keySet();
    }

    @Nonnull
    @Override
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
