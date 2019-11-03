package lyrics.songs;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since August 2018
 */
public class SongPattern
{
    public static final SongPattern ORGAN_4 = new SongPattern(
        ImmutableList.of(
            StanzaPattern.ORGAN_4_VERSE,
            StanzaPattern.ORGAN_4_VERSE,
            StanzaPattern.ORGAN_4_CHORUS,
            StanzaPattern.ORGAN_4_CHORUS
        ),
        4
    );
    public static final SongPattern F5 = new SongPattern(
        ImmutableList.of(
            StanzaPattern.F5
        ),
        4
    );

    @Nonnull
    private final List<StanzaPattern> m_stanzaPatterns;
    private final int m_numVerses;

    public SongPattern(@Nonnull List<StanzaPattern> stanzaPatterns, int numVerses)
    {
        m_stanzaPatterns = ImmutableList.copyOf(stanzaPatterns);
        m_numVerses = numVerses;
    }

    public SongPattern(@Nonnull StanzaPattern stanzaPattern, int numVerses)
    {
        m_stanzaPatterns = ImmutableList.of(stanzaPattern);
        m_numVerses = numVerses;
    }

    @Nonnull
    public List<StanzaPattern> getStanzaPatterns()
    {
        return m_stanzaPatterns;
    }

    public int getNumVerses()
    {
        return m_numVerses;
    }
}
