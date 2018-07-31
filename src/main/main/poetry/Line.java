package main.poetry;

import com.google.common.collect.ImmutableList;
import main.dictionaries.IDictionary;
import main.linguistics.Pronunciation;
import main.linguistics.Syllable;
import main.utils.StringUtils;

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
     * @throws Exception
     */
    public Line(@Nonnull String string, @Nonnull IDictionary dictionary) throws Exception
    {
        m_words = ImmutableList.copyOf(Stream.of(string.split("\\s+"))
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
        return m_words.stream().collect(Collectors.joining(" "));
    }

    @Nonnull
    public List<Integer> getMeter()
    {
        return m_syllables.stream().map(Syllable::getEmphasis).collect(Collectors.toList());
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if a word doesn't have a pronunciation in the dictionary
     */
    @Nonnull
    private static List<Syllable> _computeSyllables(@Nonnull List<String> words, @Nonnull IDictionary dictionary)
    {
        return words.stream()
            .map(dictionary::getPronunciations)
            .map(list -> list.get(0))
            .map(Pronunciation::getSyllables)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Line line = (Line) o;

        if (!m_words.equals(line.m_words))
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
