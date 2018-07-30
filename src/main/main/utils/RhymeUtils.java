package main.utils;

import com.google.common.collect.ImmutableList;
import main.linguistics.Phoneme;
import main.linguistics.Pronunciation;
import main.linguistics.Syllable;
import main.dictionaries.IDictionary;

import javax.annotation.Nonnull;
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
        int numSyllablesToMatch = Math.min(firstWord.getSyllables().size(), 2);
        if (secondWord.getSyllables().size() < numSyllablesToMatch)
        {
            return false;
        }

        for (int i = 0; i < numSyllablesToMatch; i++)
        {
            int firstIndex = firstWord.getSyllables().size() - 1 - i;
            int secondIndex = secondWord.getSyllables().size() - 1 - i;
            Syllable firstSyllable = firstWord.getSyllables().get(firstIndex);
            Syllable secondSyllable = secondWord.getSyllables().get(secondIndex);

            // Ignore initial consonants if this is the first syllable.
            if (i == (numSyllablesToMatch - 1))
            {
                return _removeInitialConsonants(firstSyllable).equals(_removeInitialConsonants(secondSyllable));
            }
            else if (!firstSyllable.equals(secondSyllable))
            {
                return false;
            }
        }
        return true;
    }


    @Nonnull
    private static List<Phoneme> _removeInitialConsonants(@Nonnull Syllable syllable)
    {
        List<Phoneme> phonemes = syllable.getPhonemes();
        int index = 0;
        while (!phonemes.get(index).isVowel())
        {
            index++;
        }
        return ImmutableList.copyOf(phonemes.subList(index, phonemes.size()));
    }

    public static boolean anyPronunciationsRhyme(@Nonnull String first, @Nonnull String second, @Nonnull IDictionary dictionary)
    {
        List<Pronunciation> firstList = dictionary.getPronunciations(first);
        List<Pronunciation> secondList = dictionary.getPronunciations(second);
        return firstList.parallelStream().anyMatch(p -> secondList.parallelStream().anyMatch(q -> rhymesWith(p, q)));
    }
}
