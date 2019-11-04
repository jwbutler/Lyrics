package lyrics.linguistics;

import com.google.common.base.Preconditions;
import lyrics.Logging;
import lyrics.Pair;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matches cmudict-0.7a.phones
 *
 * @author jbutler
 * @since July 2018
 */
public enum Phoneme
{
    /**
     * "A" as in "CALL"
     */
    AA(PhonemeType.VOWEL),
    /**
     * "A" as in "CAT"
     */
    AE(PhonemeType.VOWEL),
    /**
     * schwa
     */
    AH(PhonemeType.VOWEL),
    /**
     *
     */
    AO(PhonemeType.VOWEL),
    AW(PhonemeType.VOWEL),
    AY(PhonemeType.VOWEL),
    B(PhonemeType.STOP),
    CH(PhonemeType.AFFRICATE),
    D(PhonemeType.STOP),
    DH(PhonemeType.FRICATIVE),
    EH(PhonemeType.VOWEL),
    ER(PhonemeType.VOWEL),
    EY(PhonemeType.VOWEL),
    F(PhonemeType.FRICATIVE),
    G(PhonemeType.STOP),
    HH(PhonemeType.ASPIRATE),
    IH(PhonemeType.VOWEL),
    IY(PhonemeType.VOWEL),
    JH(PhonemeType.AFFRICATE),
    K(PhonemeType.STOP),
    L(PhonemeType.LIQUID),
    M(PhonemeType.NASAL),
    N(PhonemeType.NASAL),
    NG(PhonemeType.NASAL),
    OW(PhonemeType.VOWEL),
    OY(PhonemeType.VOWEL),
    P(PhonemeType.STOP),
    R(PhonemeType.LIQUID),
    S(PhonemeType.FRICATIVE),
    SH(PhonemeType.FRICATIVE),
    T(PhonemeType.STOP),
    TH(PhonemeType.FRICATIVE),
    UH(PhonemeType.VOWEL),
    UW(PhonemeType.VOWEL),
    V(PhonemeType.FRICATIVE),
    W(PhonemeType.SEMIVOWEL),
    Y(PhonemeType.SEMIVOWEL),
    Z(PhonemeType.FRICATIVE),
    ZH(PhonemeType.FRICATIVE);

    private final PhonemeType m_type;

    Phoneme(@Nonnull PhonemeType type)
    {
        m_type = type;
    }

    @Nonnull
    public PhonemeType getType()
    {
        return m_type;
    }

    public boolean isVowel()
    {
        return m_type.isVowel();
    }

    @CheckForNull
    public static Phoneme fromString(@Nonnull String value)
    {
        return Stream.of(values()).filter(p -> p.name().equals(value)).findFirst().orElse(null);
    }

    /**
     * @param string A representation of a phoneme, optionally including a number for emphasis
     *               This is expected to consist only of capital letters and numbers
     * @return a map of (phoneme -> emphasis), where emphasis is a nullable integer value from 0 (low) to 2 (high)
     * @throws IllegalArgumentException if the string contains unexpected characters
     *                                  or if it does not contain a phoneme value
     */
    @Nonnull
    public static Pair<Phoneme, Emphasis> getPhonemeWithEmphasis(@Nonnull String string)
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
                emphasis = Emphasis.fromValue(Integer.valueOf(string.replaceAll("[A-Z]", "")));
            }
            catch (NumberFormatException e)
            {
                Logging.debug(e.getMessage(), e);
            }
        }
        return Pair.of(phoneme, emphasis);
    }
}
