package lyrics.linguistics;

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
        return Stream.of(values())
            .filter(p -> p.name().equals(value))
            .findFirst()
            .orElse(null);
    }
}
