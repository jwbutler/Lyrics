package lyrics.poetry;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lyrics.utils.Preconditions;

import static lyrics.utils.Preconditions.checkArgument;

/**
 * @author jbutler
 * @since July 2018
 */
public record Poem
(
    @Nonnull List<List<Line>> stanzas
)
{
    public Poem
    {
        checkArgument(!stanzas.isEmpty());
        checkArgument(stanzas.stream().allMatch(Objects::nonNull));
        checkArgument(stanzas.stream().allMatch(stanza -> stanza.size() >= 2));
    }

    @Nonnull
    @Override
    public String toString()
    {
        return stanzas()
            .stream()
            .map(stanza -> stanza.stream()
                .map(Line::toString)
                .collect(Collectors.joining("\n")))
            .collect(Collectors.joining("\n\n"));
    }
}
