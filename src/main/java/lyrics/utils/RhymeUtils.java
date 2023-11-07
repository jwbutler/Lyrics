package lyrics.utils;

import lyrics.linguistics.Phoneme;
import lyrics.linguistics.Pronunciation;
import lyrics.linguistics.Syllable;
import lyrics.dictionaries.Dictionary;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public class RhymeUtils
{
    /**
     * Two words rhyme if:
     */
    public static boolean rhymesWith(@Nonnull Pronunciation firstWord, @Nonnull Pronunciation secondWord)
    {
        int numSyllablesToMatch = Math.min(firstWord.syllables().size(), 2);
        if (secondWord.syllables().size() < numSyllablesToMatch)
        {
            return false;
        }

        for (int i = 0; i < numSyllablesToMatch; i++)
        {
            int firstIndex = firstWord.syllables().size() - 1 - i;
            int secondIndex = secondWord.syllables().size() - 1 - i;
            Syllable firstSyllable = firstWord.syllables().get(firstIndex);
            Syllable secondSyllable = secondWord.syllables().get(secondIndex);

            // Ignore initial consonants if this is the first syllable.
            if (i == (numSyllablesToMatch - 1))
            {
                if (!rhymesWith(
                    _removeInitialConsonants(firstSyllable),
                    _removeInitialConsonants(secondSyllable)
                ))
                {
                    return false;
                }
            }
            else if (!rhymesWith(firstSyllable, secondSyllable))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean rhymesWith(@Nonnull Syllable firstSyllable, @Nonnull Syllable secondSyllable)
    {
        return rhymesWith(firstSyllable.phonemes(), secondSyllable.phonemes());
    }

    public static boolean rhymesWith(@Nonnull List<Phoneme> firstPhonemes, @Nonnull List<Phoneme> secondPhonemes)
    {
        if (firstPhonemes.equals(secondPhonemes))
        {
            return true;
        }

        if (stripFinalS(firstPhonemes).equals(stripFinalS(secondPhonemes)))
        {
            return true;
        }

        return false;
    }

    /**
     * TODO move me?
     */
    @Nonnull
    public static List<Phoneme> stripFinalS(@Nonnull List<Phoneme> phonemes)
    {
        if (EnumSet.of(Phoneme.S, Phoneme.Z).contains(phonemes.get(phonemes.size() - 1)))
        {
            return List.copyOf(phonemes.subList(0, phonemes.size() - 1));
        }
        return List.copyOf(phonemes);
    }

    @Nonnull
    private static List<Phoneme> _removeInitialConsonants(@Nonnull Syllable syllable)
    {
        List<Phoneme> phonemes = syllable.phonemes();
        int index = 0;
        while (!phonemes.get(index).isVowel())
        {
            index++;
        }
        return phonemes.subList(index, phonemes.size());
    }

    public static boolean anyPronunciationsRhyme(@Nonnull String first, @Nonnull String second, @Nonnull Dictionary dictionary)
    {
        List<Pronunciation> firstList = dictionary.getPronunciations(first);
        List<Pronunciation> secondList = dictionary.getPronunciations(second);
        return firstList.stream()
            .anyMatch(p -> secondList.stream()
                .anyMatch(q -> rhymesWith(p, q))
            );
    }
}
