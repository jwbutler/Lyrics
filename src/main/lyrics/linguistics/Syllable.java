package lyrics.linguistics;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jwbutler
 * @since July 2018
 */
public class Syllable
{
    public enum Type
    {
        CONSONANT_VOWEL,
        CONSONANT_VOWEL_CONSONANT,
        VOWEL_CONSONANT,
        VOWEL // This only appears at the beginning of a word
    }

    @Nonnull
    private final List<Phoneme> m_phonemes;
    @Nonnull
    private final Type m_type;
    @Nonnull
    private final Emphasis m_emphasis;

    public Syllable(@Nonnull List<Phoneme> phonemes, @Nonnull Emphasis emphasis)
    {
        m_phonemes = ImmutableList.copyOf(phonemes);
        m_type = _computeType(phonemes);
        m_emphasis = emphasis;
    }

    /**
     * @throws IllegalArgumentException if the list does not map to any Type value
     */
    @Nonnull
    private Type _computeType(@Nonnull List<Phoneme> phonemes)
    {
        if (phonemes.stream().allMatch(Phoneme::isVowel))
        {
            return Type.VOWEL;
        }
        else
        {
            if (phonemes.stream().anyMatch(Phoneme::isVowel))
            {
                if (phonemes.get(phonemes.size() - 1).isVowel())
                {
                    return Type.CONSONANT_VOWEL;
                }
                else if (!phonemes.get(0).isVowel())
                {
                    return Type.CONSONANT_VOWEL_CONSONANT;
                }
                else
                {
                    return Type.VOWEL_CONSONANT;
                }
            }
        }
        throw new IllegalArgumentException("lyrics.linguistics.Syllable " + toString() + " does not match any known syllable type");
    }

    /**
     * TODO refactor to get emphasis from the string
     *
     * @throws IllegalArgumentException if the string does not map correctly
     */
    @Nonnull
    public static Syllable of(@Nonnull String spaceSeparatedPhonemes, @CheckForNull Emphasis emphasis)
    {
        return Arrays.stream(spaceSeparatedPhonemes.split(" "))
            .map(Phoneme::valueOf)
            .collect(Collectors.collectingAndThen(Collectors.toList(),
                p -> new Syllable(p, Optional.ofNullable(emphasis).orElse(Emphasis.WEAK))
            ));
    }

    @Nonnull
    @VisibleForTesting
    public static Syllable of(@Nonnull String spaceSeparatedPhonemes)
    {
        return Syllable.of(spaceSeparatedPhonemes, Emphasis.WEAK);
    }

    @Nonnull
    public Type getType()
    {
        return m_type;
    }

    @Nonnull
    public List<Phoneme> getPhonemes()
    {
        return m_phonemes;
    }

    @Nonnull
    public Emphasis getEmphasis()
    {
        return m_emphasis;
    }

    @Nonnull
    @Override
    public String toString()
    {
        return m_phonemes.stream().map(Phoneme::toString).collect(Collectors.joining(" "));
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
        Syllable syllable = (Syllable) o;
        return m_phonemes.equals(syllable.m_phonemes) && m_type == syllable.m_type && m_emphasis == syllable.m_emphasis;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(m_phonemes, m_type, m_emphasis);
    }
}
