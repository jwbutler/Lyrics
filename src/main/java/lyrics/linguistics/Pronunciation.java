package lyrics.linguistics;

import lyrics.Logging;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static lyrics.utils.Preconditions.checkArgument;

/**
 * Represents a particular pronunciation of a word.
 * A phonetic word is defined as:
 * - a vowel group (optional)
 * - any number of (consonant group + vowel group) pairs
 * - a consonant group (optional)
 * <p>
 * Note that this does not cover all the cases.
 * For example, "nuance" has consecutive vowel clusters.
 *
 * @author jbutler
 * @since July 2018
 */
public record Pronunciation
(
    @Nonnull List<Syllable> syllables
)
{
    @Nonnull
    public static Pronunciation fromPhonemes(@Nonnull String spaceSeparatedPhonemes)
    {
        List<PhonemeWithEmphasis> phonemesWithEmphasis = Arrays.stream(spaceSeparatedPhonemes.split(" "))
            .map(Pronunciation::_phonemeWithEmphasis)
            .toList();
        var syllables = _computeSyllables(phonemesWithEmphasis);
        return new Pronunciation(syllables);
    }

    @Nonnull
    private static List<Syllable> _computeSyllables(@Nonnull List<PhonemeWithEmphasis> phonemesWithEmphasis)
    {
        // This list is in reverse order.  Reverse it after building.
        List<Syllable> syllables = new ArrayList<>();

        // Compute the first syllable separately.
        // Include the final consonant cluster, if it exists;
        // then find a vowel cluster;
        // then find a consonant cluster.
        List<Phoneme> phonemesInSyllable = new ArrayList<>();
        @CheckForNull Emphasis emphasis = null;

        int index = phonemesWithEmphasis.size() - 1;

        while (index >= 0 && !phonemesWithEmphasis.get(index).phoneme().isVowel())
        {
            if (phonemesWithEmphasis.get(index).emphasis() != null)
            {
                emphasis = phonemesWithEmphasis.get(index).emphasis();
            }
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).phoneme());
            index--;
        }
        while (index >= 0 && phonemesWithEmphasis.get(index).phoneme().isVowel())
        {
            if (phonemesWithEmphasis.get(index).emphasis() != null)
            {
                // Handle multiple consecutive vowels
                // Start a new syllable if the current phoneme is a vowel with emphasis and there's already an
                // emphasis for the current syllable
                if (emphasis != null)
                {
                    syllables.add(new Syllable(phonemesInSyllable.reversed(), emphasis));
                    phonemesInSyllable = new ArrayList<>();
                }
                emphasis = phonemesWithEmphasis.get(index).emphasis();
            }
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).phoneme());
            index--;
        }
        while (index >= 0 && !phonemesWithEmphasis.get(index).phoneme().isVowel())
        {
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).phoneme());
            index--;
        }

        syllables.add(new Syllable(phonemesInSyllable.reversed(), Optional.ofNullable(emphasis).orElse(Emphasis.NO_STRESS)));
        emphasis = null;

        // Now find any number of (consonant-vowel) syllables.
        while (index >= 0)
        {
            phonemesInSyllable = new ArrayList<>();
            while (index >= 0 && phonemesWithEmphasis.get(index).phoneme().isVowel())
            {
                if (phonemesWithEmphasis.get(index).emphasis() != null)
                {
                    // Handle multiple consecutive vowels
                    // Start a new syllable if the current phoneme is a vowel with emphasis and there's already an
                    // emphasis for the current syllable
                    if (emphasis != null)
                    {
                        syllables.add(new Syllable(phonemesInSyllable.reversed(), emphasis));
                        phonemesInSyllable = new ArrayList<>();
                    }
                    emphasis = phonemesWithEmphasis.get(index).emphasis();
                }
                phonemesInSyllable.add(phonemesWithEmphasis.get(index).phoneme());
                index--;
            }
            while (index >= 0 && !phonemesWithEmphasis.get(index).phoneme().isVowel())
            {
                phonemesInSyllable.add(phonemesWithEmphasis.get(index).phoneme());
                index--;
            }
            syllables.add(new Syllable(phonemesInSyllable.reversed(), Optional.ofNullable(emphasis).orElse(Emphasis.NO_STRESS)));
            emphasis = null;
        }
        return syllables.reversed();
    }

    /**
     * @param string A representation of a phoneme, optionally including a number for emphasis
     *               This is expected to consist only of capital letters and numbers
     * @return a map of (phoneme -> emphasis), where emphasis is a nullable integer value from 0 (low) to 2 (high)
     * @throws IllegalArgumentException if the string contains unexpected characters
     *                                  or if it does not contain a phoneme value
     */
    @Nonnull
    private static PhonemeWithEmphasis _phonemeWithEmphasis(@Nonnull String string)
    {
        checkArgument(string.matches("^[A-Z0-9]*$"));
        @CheckForNull Phoneme phoneme = Phoneme.fromString(string.replaceAll("[^A-Z]", ""));
        if (phoneme == null)
        {
            throw new IllegalArgumentException(string + " did not contain a phoneme!");
        }
        @CheckForNull Emphasis emphasis = null;
        if (string.matches(".*[0-9].*"))
        {
            try
            {
                emphasis = Emphasis.fromValue(parseInt(string.replaceAll("[A-Z]", "")));
            }
            catch (NumberFormatException e)
            {
                Logging.debug(e.getMessage(), e);
            }
        }
        return new PhonemeWithEmphasis(phoneme, emphasis);
    }

    private record PhonemeWithEmphasis(@Nonnull Phoneme phoneme, @CheckForNull Emphasis emphasis) {}
}
