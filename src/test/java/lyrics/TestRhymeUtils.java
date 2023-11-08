package lyrics;

import lyrics.linguistics.Phoneme;
import lyrics.linguistics.Pronunciation;
import lyrics.utils.RhymeUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author jbutler
 * @since July 2018
 */
public class TestRhymeUtils
{
    @Test
    public void testRhymesWith()
    {
        assertFalse(RhymeUtils.rhymesWith(
            Pronunciation.fromPhonemes("EH N T ER D"),
            Pronunciation.fromPhonemes("EH R AH N")
        ));
        Pronunciation best = Pronunciation.fromPhonemes("B EH S T");
        Pronunciation cats = Pronunciation.fromPhonemes("K AE T S");
        Pronunciation chest = Pronunciation.fromPhonemes("CH EH S T");
        Pronunciation chaste = Pronunciation.fromPhonemes("CH EY S T");
        Pronunciation say = Pronunciation.fromPhonemes("S EY");
        Pronunciation plays = Pronunciation.fromPhonemes("P L EY Z");
        Pronunciation amazing = Pronunciation.fromPhonemes("AH M EY Z IH NG");
        Pronunciation amaze = Pronunciation.fromPhonemes("AH M EY Z");
        Pronunciation good = Pronunciation.fromPhonemes("G UH D");
        Pronunciation giving = Pronunciation.fromPhonemes("G IH V IH NG");
        Stream.of(best, cats, chest, chaste, say, plays, amazing, amaze, good, giving).forEach(Assert::assertNotNull);

        assertTrue(RhymeUtils.rhymesWith(best, chest));
        assertTrue(RhymeUtils.rhymesWith(amaze, plays));
        assertFalse(RhymeUtils.rhymesWith(chaste, chest));
        assertFalse(RhymeUtils.rhymesWith(chaste, cats));
        assertFalse(RhymeUtils.rhymesWith(say, plays));
        assertFalse(RhymeUtils.rhymesWith(amaze, amazing));
        assertFalse(RhymeUtils.rhymesWith(good, giving));
    }
    
    @Test
    public void testStripFinalS()
    {
        var input = List.of(
            Phoneme.AA,
            Phoneme.S,
            Phoneme.K,
            Phoneme.S
        );
        var expected = List.of(
            Phoneme.AA,
            Phoneme.S,
            Phoneme.K
        );
        assertEquals(RhymeUtils.stripFinalS(input), expected);
    }
}
