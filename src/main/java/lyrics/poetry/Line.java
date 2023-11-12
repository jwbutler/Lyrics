package lyrics.poetry;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import lyrics.NoPronunciationException;
import lyrics.dictionaries.Dictionary;
import lyrics.linguistics.Pronunciation;
import lyrics.linguistics.Syllable;
import lyrics.meter.Meter;
import lyrics.utils.StringUtils;

/**
 * @author jbutler
 * @since July 2018
 * 
 * @param words All upper-case
 */
public record Line
(
    @Nonnull List<String> words,
    @Nonnull List<Syllable> syllables
)
{
    /**
     * @param string A space-separated list of words
     */
    @Nonnull
    public static Line fromString(
        @Nonnull String string,
        @Nonnull Dictionary dictionary
    ) throws NoPronunciationException
    {
        List<String> words = Stream.of(string.split("\\s+"))
            .map(StringUtils::alphanumericOnly)
            .map(String::toUpperCase)
            .toList();
        List<Syllable> syllables = _computeSyllables(words, dictionary);
        return new Line(words, syllables);
    }

    @Override
    @Nonnull
    public String toString()
    {
        return String.join(" ", words());
    }

    @Nonnull
    public Meter getMeter()
    {
        return Meter.forSyllables(syllables());
    }

    /**
     * @throws NoPronunciationException if a word doesn't have a pronunciation in the dictionary
     */
    @Nonnull
    private static List<Syllable> _computeSyllables(
        @Nonnull List<String> words,
        @Nonnull Dictionary dictionary
    ) throws NoPronunciationException
    {
        List<Set<Pronunciation>> pronunciations = words.stream()
            .map(dictionary::getPronunciations)
            .toList();

        for (int i = 0; i < words.size(); i++)
        {
            if (pronunciations.get(i).isEmpty())
            {
                throw new NoPronunciationException(words.get(i));
            }
        }

        return words.stream()
            .map(dictionary::getPronunciations)
            .map(set -> set.iterator().next())
            .map(Pronunciation::syllables)
            .flatMap(List::stream)
            .toList();
    }

    public boolean matches(@Nonnull Line line)
    {
        if (!words().equals(line.words()))
        {
            return false;
        }
        return syllables().equals(line.syllables());
    }
}
