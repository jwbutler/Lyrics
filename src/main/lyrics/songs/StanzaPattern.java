package lyrics.songs;

import com.google.common.collect.ImmutableList;
import lyrics.meter.Meter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since July 2018
 */
public class StanzaPattern
{
    // TODO convert these to static instances
    public static final StanzaPattern EERIE_VILLAIN = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'A', 'A', 'B')
    );
    public static final StanzaPattern GOD_BLUES = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern ORGAN_4_VERSE = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1)
        ),
        ImmutableList.of('A', 'A', 'B', 'C', 'E')
    );
    public static final StanzaPattern ORGAN_4_CHORUS = StanzaPattern.fromLists(
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
    public static final StanzaPattern THE_NAMELESS_CITY_VERSE = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern THE_NAMELESS_CITY_CHORUS = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B')
    );
    public static final StanzaPattern IAMBIC_PENTAMETER_ABAB = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern TRITONE_BLUES = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(1, 0, 0, 1, 1, 1)
        ),
        ImmutableList.of('A', 'A', 'B', 'B', 'C')
    );
    public static final StanzaPattern RIDE_ON_TO_GLORY_VERSE_1 = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern RIDE_ON_TO_GLORY_VERSE_2 = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern F5 = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern SAPPY_VERSE_1 = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'A')
    );
    public static final StanzaPattern SAPPY_VERSE_2 = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0)
        ),
        ImmutableList.of('A', 'A')
    );
    public static final StanzaPattern SAPPY_CHORUS = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 1, 0)
        ),
        ImmutableList.of('A')
    );
    public static final StanzaPattern DEC_30_68_E = StanzaPattern.fromLists(
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
    public static final StanzaPattern DEC_30_68_E_VAR1 = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 1, 0, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 1, 0, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'C', 'B')
    );
    public static final StanzaPattern DEC_30_68_E_VAR2 = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'C', 'B')
    );
    public static final StanzaPattern JESUS_KING_OF_GLORY = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );
    public static final StanzaPattern CAVEMAN = StanzaPattern.fromLists(
        ImmutableList.of(
            ImmutableList.of(0, 1, 1, 0, 0, 1),
            ImmutableList.of(0, 1, 1, 0, 0, 1),
            ImmutableList.of(0, 1, 1, 0, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    );

    @Nonnull
    private final ImmutableList<Meter> m_meters;
    @Nonnull
    private final ImmutableList<Character> m_rhymeScheme;

    public StanzaPattern(@Nonnull List<Meter> meters, @Nonnull List<Character> rhymeScheme)
    {
        m_meters = ImmutableList.copyOf(meters);
        m_rhymeScheme = ImmutableList.copyOf(rhymeScheme);
    }

    @Nonnull
    public static StanzaPattern fromLists(@Nonnull List<List<Integer>> meters, @Nonnull List<Character> rhymeScheme)
    {
        return new StanzaPattern(
            meters.stream()
                .map(list -> list.stream()
                    .mapToInt(Integer::intValue)
                    .toArray())
                .map(Meter::of)
                .collect(_toImmutableList()),
            rhymeScheme
        );
    }

    @Nonnull
    private static <T> Collector<T, ?, ImmutableList<T>> _toImmutableList()
    {
        return Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf);
    }

    @Nonnull
    public List<Meter> getMeters()
    {
        return m_meters;
    }

    @Nonnull
    public List<Character> getRhymeScheme()
    {
        return m_rhymeScheme;
    }
}