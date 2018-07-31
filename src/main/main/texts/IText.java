package main.texts;

import main.poetry.Line;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public interface IText
{
    @Nonnull Line getLine(@Nonnull List<Integer> meter);
    @CheckForNull Line getLine(@Nonnull List<Line> rhymingLines, @Nonnull List<Integer> meter);
}
