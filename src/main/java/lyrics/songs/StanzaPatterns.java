package lyrics.songs;

import java.util.List;

import lyrics.meter.Meter;

public final class StanzaPatterns
{
    private StanzaPatterns() {}
    
    public static final StanzaPattern EERIE_VILLAIN = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 0, 1, 0, 1),
            Meter.of(1, 0, 1, 0, 1, 0, 1),
            Meter.of(1, 0, 1, 0, 1, 0, 1),
            Meter.of(1, 0, 1, 0, 1)
        ),
        List.of('A', 'A', 'A', 'B')
    );
    
    public static final StanzaPattern GOD_BLUES = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 0, 1, 0, 1, 0),
            Meter.of(1, 0, 1, 0, 1, 0, 1),
            Meter.of(1, 0, 1, 0, 1, 0, 1, 0),
            Meter.of(1, 0, 1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'A', 'B')
    );
    
    public static final StanzaPattern ORGAN_4_VERSE = new StanzaPattern(
        List.of(
            Meter.of(0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(1, 0, 0, 1)
        ),
        List.of('A', 'A', 'B', 'C', 'E')
    );
    
    public static final StanzaPattern ORGAN_4_CHORUS = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 0, 1),
            Meter.of(0, 0, 1, 0, 1, 0),
            Meter.of(1, 0, 0, 1),
            Meter.of(0, 0, 1, 0, 1, 0),
            Meter.of(1, 0, 0, 1),
            Meter.of(0, 0, 1, 0, 1, 0),
            Meter.of(1, 0, 0, 1)
        ),
        List.of('A', 'B', 'C', 'B', 'D', 'E', 'F')
    );
    
    public static final StanzaPattern THE_NAMELESS_CITY_VERSE = new StanzaPattern(
        List.of(
            Meter.of(0, 1, 0, 1, 0, 1, 0),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'A', 'B')
    );
    
    public static final StanzaPattern THE_NAMELESS_CITY_CHORUS = new StanzaPattern(
        List.of(
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
        ),
        List.of('A', 'B')
    );
    
    public static final StanzaPattern IAMBIC_PENTAMETER_ABAB = new StanzaPattern(
        List.of(
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'A', 'B')
    );
    
    public static final StanzaPattern TRITONE_BLUES = new StanzaPattern(
        List.of(
            Meter.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            Meter.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            Meter.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            Meter.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            Meter.of(1, 0, 0, 1, 1, 1)
        ),
        List.of('A', 'A', 'B', 'B', 'C')
    );
    
    public static final StanzaPattern RIDE_ON_TO_GLORY_VERSE_1 = new StanzaPattern(
        List.of(
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0)
        ),
        List.of('A', 'B', 'A', 'B')
    );
    
    public static final StanzaPattern RIDE_ON_TO_GLORY_VERSE_2 = new StanzaPattern(
        List.of(
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'A', 'B')
    );
    
    public static final StanzaPattern F5 = new StanzaPattern(
        List.of(
            Meter.of(1, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(1, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'A', 'B')
    );
    
    public static final StanzaPattern SAPPY_VERSE_1 = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(1, 0, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(1, 1, 0, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        List.of('A', 'A')
    );
    
    public static final StanzaPattern SAPPY_VERSE_2 = new StanzaPattern(
        List.of(
            Meter.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0),
            Meter.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0)
        ),
        List.of('A', 'A')
    );
    
    public static final StanzaPattern SAPPY_CHORUS = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 1, 0)
        ),
        List.of('A')
    );
    
    public static final StanzaPattern DEC_30_68_E = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 0, 1),
            Meter.of(1, 0, 0, 1),
            Meter.of(1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1),
            Meter.of(1, 0, 0, 1),
            Meter.of(1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'C', 'D', 'E', 'C')
    );
    
    public static final StanzaPattern DEC_30_68_E_VAR1 = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 0, 1, 1, 0, 0, 1),
            Meter.of(1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1, 1, 0, 0, 1),
            Meter.of(1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'C', 'B')
    );
    
    public static final StanzaPattern DEC_30_68_E_VAR2 = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 0, 1),
            Meter.of(1, 0, 0, 1, 1, 0, 1, 0, 1),
            Meter.of(0, 1, 0, 1),
            Meter.of(1, 0, 0, 1, 1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'C', 'B')
    );
    
    public static final StanzaPattern JESUS_KING_OF_GLORY = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 0, 1, 0),
            Meter.of(1, 0, 1, 0, 1),
            Meter.of(1, 0, 1, 0, 1, 0),
            Meter.of(1, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'A', 'B')
    );
    
    public static final StanzaPattern CAVEMAN = new StanzaPattern(
        List.of(
            Meter.of(0, 1, 1, 0, 0, 1),
            Meter.of(0, 1, 1, 0, 0, 1),
            Meter.of(0, 1, 1, 0, 0, 1),
            Meter.of(0, 1, 0, 1, 0, 1, 1)
        ),
        List.of('A', 'B', 'A', 'B')
    );
    
    public static final StanzaPattern SPACE_DAGGER = new StanzaPattern(
        List.of(
            Meter.of(1, 0, 1, 0, 0, 1, 0),
            Meter.of(1, 0, 1, 0, 0, 1, 0, 1),
            Meter.of(1, 0, 1, 0, 0, 1, 0),
            Meter.of(1, 0, 1, 0, 0, 1, 0, 1)
        ),
        List.of('A', 'B', 'A', 'B')
    );
}
