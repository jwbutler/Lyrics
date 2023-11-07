package lyrics.texts;

import lyrics.meter.Meter;
import lyrics.poetry.Line;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jwbutler
 * @since July 2018
 */
public interface LineSupplier
{
    @CheckForNull Line getLine(@Nonnull Meter meter);
    @CheckForNull Line getLine(@Nonnull List<Line> rhymingLines, @Nonnull Meter meter);
}
