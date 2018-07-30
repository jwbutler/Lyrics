package main.poetry;

import com.google.common.base.Preconditions;
import main.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since July 2018
 */
public class Poem
{
    List<List<Line>> m_stanzas;
    private Pair<Integer, Integer> m_meter;

    public Poem(@Nonnull List<List<Line>> stanzas)
    {
        Preconditions.checkArgument(!stanzas.isEmpty());
        Preconditions.checkArgument(stanzas.stream().allMatch(Objects::nonNull));
        Preconditions.checkArgument(stanzas.stream().allMatch(stanza -> stanza.size() >= 2));
        m_stanzas = stanzas;
        m_meter = Pair.of(
            stanzas.get(0).get(0).getMeter().size(),
            stanzas.get(0).get(1).getMeter().size()
        );
    }

    @Nonnull
    List<List<Line>> getStanzas()
    {
        return m_stanzas;
    }

    @Nonnull
    @Override
    public String toString()
    {
        return m_stanzas.stream()
            .map(stanza -> stanza.stream()
                .map(Line::toString)
                .map(String::toUpperCase)
                .collect(Collectors.joining("\n")))
            .collect(Collectors.joining("\n\n"));
    }
}
