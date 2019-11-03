package lyrics;

import lyrics.linguistics.Pronunciation;
import lyrics.utils.RhymeUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.Stream;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author jbutler
 * @since July 2018
 */
public class TestRhymeUtils {
  @Test
  public void testRhymesWith() {
    assertFalse(RhymeUtils.rhymesWith(
      new Pronunciation("EH N T ER D"), new Pronunciation("EH R AH N")
    ));
    Pronunciation best = new Pronunciation("B EH S T");
    Pronunciation cats = new Pronunciation("K AE T S");
    Pronunciation chest = new Pronunciation("CH EH S T");
    Pronunciation chaste = new Pronunciation("CH EY S T");
    Pronunciation say = new Pronunciation("S EY");
    Pronunciation plays = new Pronunciation("P L EY Z");
    Pronunciation amazing = new Pronunciation("AH M EY Z IH NG");
    Pronunciation amaze = new Pronunciation("AH M EY Z");
    Pronunciation good = new Pronunciation("G UH D");
    Pronunciation giving = new Pronunciation("G IH V IH NG");
    Stream.of(best, cats, chest, chaste, say, plays, amazing, amaze, good, giving)
      .forEach(Assert::assertNotNull);

    assertTrue(RhymeUtils.rhymesWith(best, chest));
    assertTrue(RhymeUtils.rhymesWith(amaze, plays));
    assertFalse(RhymeUtils.rhymesWith(chaste, chest));
    assertFalse(RhymeUtils.rhymesWith(chaste, cats));
    assertFalse(RhymeUtils.rhymesWith(say, plays));
    assertFalse(RhymeUtils.rhymesWith(amaze, amazing));
    assertFalse(RhymeUtils.rhymesWith(good, giving));
  }
}
