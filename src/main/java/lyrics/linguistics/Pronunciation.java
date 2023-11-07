package lyrics.linguistics;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lyrics.Logging;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

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
public class Pronunciation
{
    @Nonnull
    private final List<Syllable> m_syllables;

    public Pronunciation(@Nonnull String spaceSeparatedPhonemes)
    {
        List<PhonemeWithEmphasis> phonemesWithEmphasis = Arrays.stream(spaceSeparatedPhonemes.split(" "))
            .map(Pronunciation::_getPhonemeWithEmphasis)
            .collect(Collectors.toList());
        m_syllables = _computeSyllables(phonemesWithEmphasis);
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

        while (index >= 0 && !phonemesWithEmphasis.get(index).getPhoneme().isVowel())
        {
            if (phonemesWithEmphasis.get(index).getEmphasis() != null)
            {
                emphasis = phonemesWithEmphasis.get(index).getEmphasis();
            }
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).getPhoneme());
            index--;
        }
        while (index >= 0 && phonemesWithEmphasis.get(index).getPhoneme().isVowel())
        {
            if (phonemesWithEmphasis.get(index).getEmphasis() != null)
            {
                // Handle multiple consecutive vowels
                // Start a new syllable if the current phoneme is a vowel with emphasis and there's already an
                // emphasis for the current syllable
                if (emphasis != null)
                {
                    syllables.add(new Syllable(Lists.reverse(phonemesInSyllable), emphasis));
                    phonemesInSyllable = new ArrayList<>();
                }
                emphasis = phonemesWithEmphasis.get(index).getEmphasis();
            }
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).getPhoneme());
            index--;
        }
        while (index >= 0 && !phonemesWithEmphasis.get(index).getPhoneme().isVowel())
        {
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).getPhoneme());
            index--;
        }

        syllables.add(new Syllable(Lists.reverse(phonemesInSyllable), Optional.ofNullable(emphasis).orElse(Emphasis.WEAK)));
        emphasis = null;

        // Now find any number of (consonant-vowel) syllables.
        while (index >= 0)
        {
            phonemesInSyllable = new ArrayList<>();
            while (index >= 0 && phonemesWithEmphasis.get(index).getPhoneme().isVowel())
            {
                if (phonemesWithEmphasis.get(index).getEmphasis() != null)
                {
                    // Handle multiple consecutive vowels
                    // Start a new syllable if the current phoneme is a vowel with emphasis and there's already an
                    // emphasis for the current syllable
                    if (emphasis != null)
                    {
                        syllables.add(new Syllable(Lists.reverse(phonemesInSyllable), emphasis));
                        phonemesInSyllable = new ArrayList<>();
                    }
                    emphasis = phonemesWithEmphasis.get(index).getEmphasis();
                }
                phonemesInSyllable.add(phonemesWithEmphasis.get(index).getPhoneme());
                index--;
            }
            while (index >= 0 && !phonemesWithEmphasis.get(index).getPhoneme().isVowel())
            {
                phonemesInSyllable.add(phonemesWithEmphasis.get(index).getPhoneme());
                index--;
            }
            syllables.add(new Syllable(Lists.reverse(phonemesInSyllable), Optional.ofNullable(emphasis).orElse(Emphasis.WEAK)));
            emphasis = null;
        }
        return Lists.reverse(syllables);
    }

    @Nonnull
    public List<Syllable> getSyllables()
    {
        return m_syllables;
    }

    /**
     * @param string A representation of a phoneme, optionally including a number for emphasis
     *               This is expected to consist only of capital letters and numbers
     * @return a map of (phoneme -> emphasis), where emphasis is a nullable integer value from 0 (low) to 2 (high)
     * @throws IllegalArgumentException if the string contains unexpected characters
     *                                  or if it does not contain a phoneme value
     */
    @Nonnull
    private static PhonemeWithEmphasis _getPhonemeWithEmphasis(@Nonnull String string)
    {
        Preconditions.checkArgument(string.matches("^[A-Z0-9]*$"));
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

    @Immutable
    private static class PhonemeWithEmphasis
    {
        @Nonnull
        private final Phoneme m_phoneme;
        @CheckForNull
        private final Emphasis m_emphasis;

        private PhonemeWithEmphasis(@Nonnull Phoneme phoneme, @CheckForNull Emphasis emphasis)
        {
            m_phoneme = phoneme;
            m_emphasis = emphasis;
        }

        @Nonnull
        private Phoneme getPhoneme()
        {
            return m_phoneme;
        }

        @CheckForNull
        private Emphasis getEmphasis()
        {
            return m_emphasis;
        }
    }
}
