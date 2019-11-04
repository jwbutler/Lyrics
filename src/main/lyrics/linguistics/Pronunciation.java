package lyrics.linguistics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lyrics.Pair;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final List<Syllable> m_syllables;

    public Pronunciation(@Nonnull String spaceSeparatedPhonemes)
    {
        List<Pair<Phoneme, Emphasis>> phonemesWithEmphasis = Arrays.stream(spaceSeparatedPhonemes.split(" ")).map(Phoneme::getPhonemeWithEmphasis).collect(Collectors.toList());
        m_syllables = _computeSyllables(phonemesWithEmphasis);
    }

    @Nonnull
    private static List<Syllable> _computeSyllables(@Nonnull List<Pair<Phoneme, Emphasis>> phonemesWithEmphasis)
    {
        // This Builder is in reverse order.  Reverse it after building.
        ImmutableList.Builder<Syllable> syllables = new ImmutableList.Builder<>();

        // Compute the first syllable separately.
        // Include the final consonant cluster, if it exists;
        // then find a vowel cluster;
        // then find a consonant cluster.
        List<Phoneme> phonemesInSyllable = new ArrayList<>();
        @CheckForNull Emphasis emphasis = null;

        int index = phonemesWithEmphasis.size() - 1;

        while (index >= 0 && !phonemesWithEmphasis.get(index).getFirst().isVowel())
        {
            if (phonemesWithEmphasis.get(index).getSecond() != null)
            {
                emphasis = phonemesWithEmphasis.get(index).getSecond();
            }
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).getFirst());
            index--;
        }
        while (index >= 0 && phonemesWithEmphasis.get(index).getFirst().isVowel())
        {
            if (phonemesWithEmphasis.get(index).getSecond() != null)
            {
                // Handle multiple consecutive vowels
                // Start a new syllable if the current phoneme is a vowel with emphasis and there's already an
                // emphasis for the current syllable
                if (emphasis != null)
                {
                    syllables.add(new Syllable(Lists.reverse(phonemesInSyllable), emphasis));
                    phonemesInSyllable = new ArrayList<>();
                }
                emphasis = phonemesWithEmphasis.get(index).getSecond();
            }
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).getFirst());
            index--;
        }
        while (index >= 0 && !phonemesWithEmphasis.get(index).getFirst().isVowel())
        {
            phonemesInSyllable.add(phonemesWithEmphasis.get(index).getFirst());
            index--;
        }

        syllables.add(new Syllable(Lists.reverse(phonemesInSyllable), Optional.ofNullable(emphasis).orElse(Emphasis.WEAK)));
        emphasis = null;

        // Now find any number of (consonant-vowel) syllables.
        while (index >= 0)
        {
            phonemesInSyllable = new ArrayList<>();
            while (index >= 0 && phonemesWithEmphasis.get(index).getFirst().isVowel())
            {
                if (phonemesWithEmphasis.get(index).getSecond() != null)
                {
                    // Handle multiple consecutive vowels
                    // Start a new syllable if the current phoneme is a vowel with emphasis and there's already an
                    // emphasis for the current syllable
                    if (emphasis != null)
                    {
                        syllables.add(new Syllable(Lists.reverse(phonemesInSyllable), emphasis));
                        phonemesInSyllable = new ArrayList<>();
                    }
                    emphasis = phonemesWithEmphasis.get(index).getSecond();
                }
                phonemesInSyllable.add(phonemesWithEmphasis.get(index).getFirst());
                index--;
            }
            while (index >= 0 && !phonemesWithEmphasis.get(index).getFirst().isVowel())
            {
                phonemesInSyllable.add(phonemesWithEmphasis.get(index).getFirst());
                index--;
            }
            syllables.add(new Syllable(Lists.reverse(phonemesInSyllable), Optional.ofNullable(emphasis).orElse(Emphasis.WEAK)));
            emphasis = null;
        }
        return syllables.build().reverse();
    }

    @Nonnull
    public List<Syllable> getSyllables()
    {
        return m_syllables;
    }
}
