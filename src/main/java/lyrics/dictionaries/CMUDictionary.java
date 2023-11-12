package lyrics.dictionaries;

import lyrics.utils.FileUtils;
import lyrics.Logging;
import lyrics.linguistics.Pronunciation;
import lyrics.utils.StringUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * @author jbutler
 * @since July 2018
 */
public final class CMUDictionary implements Dictionary
{
    private static final String SPLIT_PATTERN = "  ";

    @Nonnull
    private final Map<String, Set<Pronunciation>> m_words;

    public CMUDictionary()
    {
        Logging.info("CMUDictionary - Loading...");
        long startTime = System.currentTimeMillis();
        m_words = new HashMap<>();
        
        var parsedLines = FileUtils.getBufferedReader("cmudict.0.7a")
            .lines()
            .map(CMUDictionary::_parseLine)
            .filter(Objects::nonNull)
            .toList();
        
        for (var parsedLine : parsedLines)
        {
            var word = parsedLine.word();
            var pronunciation = parsedLine.pronunciation();
            m_words.computeIfAbsent(word, x -> new HashSet<>()).add(pronunciation);
        }
        
        long endTime = System.currentTimeMillis();
        Logging.infoF("CMUDictionary - Loaded %d words in %d ms\n", m_words.size(), (endTime - startTime));
    }

    @Nonnull
    @Override
    public Set<String> getWords()
    {
        return m_words.keySet();
    }

    @Nonnull
    @Override
    public Set<Pronunciation> getPronunciations(@Nonnull String key)
    {
        return m_words.getOrDefault(key, emptySet());
    }

    /**
     * @return a pair of (key, list of phonemes)
     * or null if it's not a valid line
     */
    @CheckForNull
    private static ParsedLine _parseLine(@Nonnull String line)
    {
        try
        {
            if (_isValidLine(line))
            {
                int index = line.indexOf(SPLIT_PATTERN);
                String word = StringUtils.alphabeticOnly(line.substring(0, index)).toUpperCase();
                String phonemes = line.substring(index + 2);

                return new ParsedLine(
                    word,
                    Pronunciation.fromPhonemes(phonemes)
                );
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

    /**
     * @param word Must be uppercase
     */
    private record ParsedLine(@Nonnull String word, @Nonnull Pronunciation pronunciation) {}
}
