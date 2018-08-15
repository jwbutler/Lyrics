package main.songs;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public enum StanzaPattern
{
    EERIE_VILLAIN(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'A', 'A', 'B')
    ),
    GOD_BLUES(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    ),
    ORGAN_4_VERSE(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1)
        ),
        ImmutableList.of('A', 'A', 'B', 'C', 'E')
    ),
    ORGAN_4_CHORUS(
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
    ),
    THE_NAMELESS_CITY_VERSE(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    ),
    THE_NAMELESS_CITY_CHORUS(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B')
    ),
    IAMBIC_PENTAMETER_ABAB(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    ),
    TRITONE_BLUES(
        ImmutableList.of(
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1),
            ImmutableList.of(1, 0, 0, 1, 1, 1)
        ),
        ImmutableList.of('A', 'A', 'B', 'B', 'C')
    ),
    RIDE_ON_TO_GLORY_VERSE_1(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    ),
    RIDE_ON_TO_GLORY_VERSE_2(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    ),
    F5(
        ImmutableList.of(
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'A', 'B')
    ),
    SAPPY_VERSE_1(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(1, 1, 0, 1, 0, 1, 0, 1),
            ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'A')
    ),
    SAPPY_VERSE_2(
        ImmutableList.of(
            ImmutableList.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0),
            ImmutableList.of(0, 1, 0, 1, 1, 0, 1, 0, 1, 0)
        ),
        ImmutableList.of('A', 'A')
    ),
    SAPPY_CHORUS(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 1, 0)
        ),
        ImmutableList.of('A')
    ),
    DEC_30_68_E(
        ImmutableList.of(
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1),
            ImmutableList.of(1, 0, 0, 1),
            ImmutableList.of(1, 0, 1, 0, 1)
        ),
        ImmutableList.of('A', 'B', 'C', 'D', 'E', 'C')
    );

    private List<List<Integer>> m_meter;
    private List<Character> m_rhymeScheme;

    StanzaPattern(@Nonnull List<List<Integer>> meter, @Nonnull List<Character> rhymeScheme)
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