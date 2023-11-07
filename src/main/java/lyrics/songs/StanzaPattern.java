package lyrics.songs;

import lyrics.meter.Meter;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public record StanzaPattern
(
    @Nonnull List<Meter> meters,
    @Nonnull List<Character> rhymeScheme
)
{
}