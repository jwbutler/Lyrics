package lyrics.songs;

import java.util.List;

public final class SongPatterns
{
    private SongPatterns() {}

    public static final SongPattern ORGAN_4 = new SongPattern(
        List.of(
            StanzaPatterns.ORGAN_4_VERSE,
            StanzaPatterns.ORGAN_4_VERSE,
            StanzaPatterns.ORGAN_4_CHORUS,
            StanzaPatterns.ORGAN_4_CHORUS
        ),
        4
    );
    public static final SongPattern F5 = new SongPattern(
        List.of(
            StanzaPatterns.F5
        ),
        4
    );
}
