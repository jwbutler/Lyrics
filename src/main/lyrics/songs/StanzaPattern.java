package lyrics.songs;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public class StanzaPattern
{
    // TODO convert these to static instances
    public static final StanzaPattern EERIE_VILLAIN = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'A', 'A', 'B')
    );
    public static final StanzaPattern GOD_BLUES = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern ORGAN_4_VERSE = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1)
        ),
        ImmutableList.of('A', 'A', 'B', 'C', 'E')
    );
    public static final StanzaPattern ORGAN_4_CHORUS = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'C', 'B', 'D', 'E', 'F')
    );
    public static final StanzaPattern THE_NAMELESS_CITY_VERSE = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern THE_NAMELESS_CITY_CHORUS = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B')
    );
    public static final StanzaPattern IAMBIC_PENTAMETER_ABAB = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern TRITONE_BLUES = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(1, 0, 0, 1, 1, 1)
        ),
        ImmutableList.of('A', 'A', 'B', 'B', 'C')
    );
    public static final StanzaPattern RIDE_ON_TO_GLORY_VERSE_1 = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern RIDE_ON_TO_GLORY_VERSE_2 = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern F5 = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern SAPPY_VERSE_1 = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'A')
    );
    public static final StanzaPattern SAPPY_VERSE_2 = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0)
        ),
        ImmutableList.of('A', 'A')
    );
    public static final StanzaPattern SAPPY_CHORUS = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 1, 0)
        ),
        ImmutableList.of('A')
    );
    public static final StanzaPattern DEC_30_68_E = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'C', 'D', 'E', 'C')
    );
    public static final StanzaPattern DEC_30_68_E_VAR1 = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 1, 0, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 1, 0, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'C', 'B')
    );
    public static final StanzaPattern DEC_30_68_E_VAR2 = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'C', 'B')
    );
    public static final StanzaPattern JESUS_KING_OF_GLORY = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern CAVEMAN = new StanzaPattern(
        ImmutableList.of(
            ImmutableList.of(0, 1, 1, 0, 0, 1),
            ImmutableList.of(0, 1, 1, 0, 0, 1),
            ImmutableList.of(0, 1, 1, 0, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );

    private List<List<Integer>> m_meter;
    private List<Character> m_rhymeScheme;

    public StanzaPattern(@Nonnull List<List<Integer>> meter, @Nonnull List<Character> rhymeScheme)
    {
        m_meter = meter;
        m_rhymeScheme = rhymeScheme;
    }

    @Nonnull
    public List<List<Integer>> getMeter()
    {
        return m_meter;
    }

    @Nonnull
    public List<Character> getRhymeScheme()
    {
        return m_rhymeScheme;
    }
}