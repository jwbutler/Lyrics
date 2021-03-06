package lyrics;

import com.google.common.collect.ImmutableList;
import lyrics.linguistics.Emphasis;
import lyrics.linguistics.Pronunciation;
import lyrics.linguistics.Syllable;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * @author jbutler
 * @since July 2018
 */
public class TestPronunciation
{
  @Test
  public void testSyllables() {
    // Syllable
    Pronunciation word = new Pronunciation("S IH L AH B AH L");
    List<Syllable> expectedSyllables = ImmutableList.of(
      Syllable.of("S IH"),
      Syllable.of("L AH"),
      Syllable.of("B AH L")
    );
    assertEquals(word.getSyllables(), expectedSyllables);

    // Amazing
    word = new Pronunciation("AH M EY Z IH NG");
    expectedSyllables = ImmutableList.of(
      Syllable.of("AH"),
      Syllable.of("M EY"),
      Syllable.of("Z IH NG")
    );
    assertEquals(word.getSyllables(), expectedSyllables);

    // Amazing
    word = new Pronunciation("AH M EY Z IH NG");
    expectedSyllables = ImmutableList.of(
        Syllable.of("AH"),
        Syllable.of("M EY"),
        Syllable.of("Z IH NG")
    );
    assertEquals(word.getSyllables(), expectedSyllables);

    // Creation
    word = new Pronunciation("K R IY0 EY1 SH AH0 N");
    expectedSyllables = ImmutableList.of(
        Syllable.of("K R IY", Emphasis.WEAK),
        Syllable.of("EY", Emphasis.STRONG),
        Syllable.of("SH AH N", Emphasis.WEAK)
    );
    assertEquals(word.getSyllables(), expectedSyllables);

    // Beings
    word = new Pronunciation("B IY1 IH0 NG Z");
    expectedSyllables = ImmutableList.of(
        Syllable.of("B IY", Emphasis.STRONG),
        Syllable.of("IH NG Z", Emphasis.WEAK)
    );
    assertEquals(word.getSyllables(), expectedSyllables);
  }
}
