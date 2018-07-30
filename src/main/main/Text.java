package main;

import com.google.common.collect.ImmutableList;
import main.poetry.Line;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
@Immutable
public class Text {
  private final List<Line> m_lines;
  private final List<String> m_words;

  public Text(@Nonnull List<Line> lines, @Nonnull List<String> words) {
    m_lines = ImmutableList.copyOf(lines);
    m_words = ImmutableList.copyOf(words);
  }

  @Nonnull
  public List<Line> getLines() {
    return m_lines;
  }
}
