package lyrics;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lyrics.dictionaries.Dictionary;
import lyrics.meter.Meter;
import lyrics.poetry.Line;
import lyrics.poetry.Poem;
import lyrics.texts.LineSupplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author jbutler
 * @since July 2018
 */
public class PoemGenerator
{
    @Nonnull
    private final Dictionary m_dictionary;
    @Nonnull
    private final List<LineSupplier> m_lineSuppliers;

    public PoemGenerator(@Nonnull Dictionary dictionary, @Nonnull List<LineSupplier> lineSuppliers)
    {
        m_dictionary = dictionary;
        m_lineSuppliers = ImmutableList.copyOf(lineSuppliers);
    }

    @Nonnull
    public Poem generatePoem(@Nonnull List<Meter> lineMeters, @Nonnull List<Character> rhymeScheme, int numStanzas)
    {
        Preconditions.checkArgument(lineMeters.size() > 0 && rhymeScheme.size() > 0);
        Preconditions.checkArgument(lineMeters.size() == rhymeScheme.size());
        ImmutableList.Builder<List<Line>> stanzas = new ImmutableList.Builder<>();
        IntStream.range(0, numStanzas)
            .parallel()
            .forEach(i ->
            {
                List<Line> stanza = _generateStanza(lineMeters, rhymeScheme);
                stanzas.add(stanza);
            });
        return new Poem(stanzas.build());
    }

    @Nonnull
    private List<Line> _generateStanza(@Nonnull List<Meter> lineMeters, @Nonnull List<Character> rhymeScheme)
    {
        Random RNG = ThreadLocalRandom.current();
        List<Line> lines = new ArrayList<>();
        while (lines.size() < lineMeters.size())
        {
            lines.clear();
            for (int i = 0; i < lineMeters.size(); i++)
            {
                LineSupplier lineSupplier = m_lineSuppliers.get(RNG.nextInt(m_lineSuppliers.size()));
                ImmutableList.Builder<Line> builder = new ImmutableList.Builder<>();
                for (int j = 0; j < i; j++)
                {
                    if (rhymeScheme.get(j) == rhymeScheme.get(i))
                    {
                        builder.add(lines.get(j));
                    }
                }
                List<Line> previousLines = builder.build();
                if (previousLines.isEmpty())
                {
                    lines.add(lineSupplier.getLine(lineMeters.get(i)));
                }
                else
                {
                    @CheckForNull Line rhymingLine = lineSupplier.getLine(previousLines, lineMeters.get(i));
                    if (rhymingLine == null)
                    {
                        // throw out this stanza and start fresh
                        break;
                    }
                    else
                    {
                        lines.add(rhymingLine);
                    }
                }
            }
        }
        return ImmutableList.copyOf(lines);
    }
}
