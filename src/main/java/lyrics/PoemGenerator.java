package lyrics;

import com.google.common.base.Preconditions;
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
    private final List<LineSupplier> m_lineSuppliers;

    public PoemGenerator(@Nonnull List<LineSupplier> lineSuppliers)
    {
        m_lineSuppliers = lineSuppliers;
    }

    @Nonnull
    public Poem generatePoem(@Nonnull List<Meter> lineMeters, @Nonnull List<Character> rhymeScheme, int numStanzas)
    {
        Preconditions.checkArgument(!lineMeters.isEmpty());
        Preconditions.checkArgument(!rhymeScheme.isEmpty());
        Preconditions.checkArgument(lineMeters.size() == rhymeScheme.size());
        List<List<Line>> stanzas = new ArrayList<>();

        for (int i = 0; i < numStanzas; i++)
        {
            List<Line> stanza = _generateStanza(lineMeters, rhymeScheme);
            stanzas.add(stanza);
        }
        return new Poem(stanzas);
    }

    @Nonnull
    private List<Line> _generateStanza(@Nonnull List<Meter> lineMeters, @Nonnull List<Character> rhymeScheme)
    {
        long t1 = System.currentTimeMillis();
        Random RNG = ThreadLocalRandom.current();
        List<Line> lines = new ArrayList<>();

        while (lines.size() < lineMeters.size())
        {
            lines.clear();
            for (int i = 0; i < lineMeters.size(); i++)
            {
                LineSupplier lineSupplier = m_lineSuppliers.get(RNG.nextInt(m_lineSuppliers.size()));
                List<Line> previousLines = new ArrayList<>();
                for (int j = 0; j < i; j++)
                {
                    if (rhymeScheme.get(j) == rhymeScheme.get(i))
                    {
                        previousLines.add(lines.get(j));
                    }
                }
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
        long t2 = System.currentTimeMillis();
        System.out.println("Generated stanza in " + (t2 - t1) + " ms");
        return lines;
    }
}
