package lyrics.linguistics;

/**
 * Matches cmudict-0.7a.phones
 *
 * @author jbutler
 * @since July 2018
 */
public enum PhonemeType
{
    AFFRICATE,
    ASPIRATE,
    FRICATIVE,
    LIQUID,
    NASAL,
    VOWEL,
    SEMIVOWEL,
    STOP;

    public boolean isVowel()
    {
        return (this == VOWEL);
    }
}
