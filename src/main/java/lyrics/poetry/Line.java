package lyrics.poetry;

import lyrics.dictionaries.Dictionary;
import lyrics.linguistics.Pronunciation;
import lyrics.linguistics.Syllable;
import lyrics.meter.Meter;
import lyrics.utils.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jbutler
 * @since July 2018
 */
public class Line
{
    @Nonnull
    private final List<String> m_words;
    @Nonnull
    private final List<Syllable> m_syllables;

    /**
     * @param string A space-separated list of words
     * @throws Exception
     */
    public Line(@Nonnull String string, @Nonnull Dictionary dictionary) throws Exception
    {
        m_words = List.copyOf(Stream.of(string.split("\\s+"))
            .map(StringUtils::alphanumericOnly)
            .collect(Collectors.toList()));
        m_syllables = _computeSyllables(m_words, dictionary);
    }

    @Nonnull
    public List<String> getWords()
    {
        return m_words;
    }

    @Override
    @Nonnull
    public String toString()
    {
        return String.join(" ", m_words);
    }

    @Nonnull
    public Meter getMeter()
    {
        return Meter.forSyllables(m_syllables);
    }

    /**
     * @throws IllegalStateException if a word doesn't have a pronunciation in the dictionary
     */
    @Nonnull
    private static List<Syllable> _computeSyllables(@Nonnull List<String> words, @Nonnull Dictionary dictionary)
    {
        List<List<Pronunciation>> pronunciations = words.stream()
            .map(dictionary::getPronunciations)
            .collect(Collectors.toList());

        for (int i = 0; i < words.size(); i++)
        {
            if (pronunciations.get(i).isEmpty())
            {
                throw new IllegalStateException("No pronunciations found for " + words.get(i));
            }
        }

        return words.stream()
            .map(dictionary::getPronunciations)
            .map(list -> list.get(0))
            .map(Pronunciation::getSyllables)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public boolean matches(@Nonnull Line line)
    {
        if (!m_words.stream().map(String::toUpperCase).collect(Collectors.toList())
            .equals(line.m_words.stream().map(String::toUpperCase).collect(Collectors.toList())))
        {
            return false;
        }
        return m_syllables.equals(line.m_syllables);
    }

    @Override
    public int hashCode()
    {
        int result = m_words.hashCode();
        result = 31 * result + m_syllables.hashCode();
        return result;
    }
}
