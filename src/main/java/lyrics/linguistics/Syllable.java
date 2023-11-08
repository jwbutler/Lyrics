package lyrics.linguistics;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jwbutler
 * @since July 2018
 */
public record Syllable
(
    @Nonnull List<Phoneme> phonemes,
    @Nonnull Type type,
    @Nonnull Emphasis emphasis
)
{
    public enum Type
    {
        CONSONANT_VOWEL,
        CONSONANT_VOWEL_CONSONANT,
        VOWEL_CONSONANT,
        VOWEL // This only appears at the beginning of a word
    }

    public Syllable(@Nonnull List<Phoneme> phonemes, @Nonnull Emphasis emphasis)
    {
        this(phonemes, _computeType(phonemes), emphasis);
    }

    /**
     * @throws IllegalArgumentException if the list does not map to any Type value
     */
    @Nonnull
    private static Type _computeType(@Nonnull List<Phoneme> phonemes)
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
        throw new IllegalArgumentException("Invalid syllable: " + phonemes);
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
                p -> new Syllable(p, Optional.ofNullable(emphasis).orElse(Emphasis.NO_STRESS))
            ));
    }

    @Nonnull
    // @VisibleForTesting
    public static Syllable of(@Nonnull String spaceSeparatedPhonemes)
    {
        return Syllable.of(spaceSeparatedPhonemes, Emphasis.NO_STRESS);
    }
}
