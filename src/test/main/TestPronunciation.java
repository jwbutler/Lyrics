package main;

import com.google.common.collect.ImmutableList;
import main.linguistics.Pronunciation;
import main.linguistics.Syllable;
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
        Syllable.of("K R IY", 0),
        Syllable.of("EY", 1),
        Syllable.of("SH AH N", 0)
    );
    assertEquals(word.getSyllables(), expectedSyllables);

    // Beings
    word = new Pronunciation("B IY1 IH0 NG Z");
    expectedSyllables = ImmutableList.of(
        Syllable.of("B IY", 1),
        Syllable.of("IH NG Z", 0)
    );
    assertEquals(word.getSyllables(), expectedSyllables);
  }

}
