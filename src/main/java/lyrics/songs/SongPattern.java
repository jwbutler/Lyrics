package lyrics.songs;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since August 2018
 */
public record SongPattern
(
    @Nonnull List<StanzaPattern> stanzaPatterns,
    int numVerses
)
{
}
