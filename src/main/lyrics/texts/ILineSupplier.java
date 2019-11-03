package lyrics.texts;

import lyrics.poetry.Line;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public interface ILineSupplier
{
    @CheckForNull Line getLine(@Nonnull List<Integer> meter);
    @CheckForNull Line getLine(@Nonnull List<Line> rhymingLines, @Nonnull List<Integer> meter);
}
