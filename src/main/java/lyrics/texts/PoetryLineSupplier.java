package lyrics.texts;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Preconditions;
import lyrics.RhymeMap;
import lyrics.dictionaries.Dictionary;
import lyrics.meter.Meter;
import lyrics.poetry.Line;

/**
 * A text consisting of a collection of discrete lines.
 *
 * @author jbutler
 * @since July 2018
 */
@Immutable
public class PoetryLineSupplier implements LineSupplier
{
    @Nonnull
    private final Set<Line> m_lines;
    @Nonnull
    private final RhymeMap m_rhymeMap;
    /**
     * a map of (meter -> (last word -> lines))
     */
    @Nonnull
    private final Map<Meter, Map<String, Set<Line>>> m_linesByMeter;

    public PoetryLineSupplier(@Nonnull Dictionary dictionary, @Nonnull Set<Line> lines)
    {
        m_lines = lines;
        m_rhymeMap = new RhymeMap(dictionary);
        m_linesByMeter = new HashMap<>();
    }

    @Override
    @CheckForNull
    public Line getLine(@Nonnull Meter meter)
    {
        Map<String, Set<Line>> matchingLineMap = _getLinesByMeter(meter);

        if (matchingLineMap.isEmpty())
        {
            throw new IllegalStateException("No lines found for meter " + meter);
        }

        List<Line> lines = matchingLineMap.values()
            .stream()
            .flatMap(Set::stream)
            .toList();

        Random RNG = ThreadLocalRandom.current();
        int index = RNG.nextInt(lines.size());
        return lines.get(index);
    }

    @Override
    @CheckForNull
    public Line getLine(@Nonnull List<Line> previousLines, @Nonnull Meter meter)
    {
        Preconditions.checkArgument(!previousLines.isEmpty());

        Line firstLine = previousLines.get(0);
        String lastWordOfFirstLine = firstLine.words().getLast();
        Set<String> rhymingWords = m_rhymeMap.getRhymes(lastWordOfFirstLine);

        if (rhymingWords.isEmpty())
        {
            return null;
        }

        Map<String, Set<Line>> lines = _getLinesByMeter(meter);

        List<Line> matchingLines = lines.entrySet()
            .stream()
            .filter(e -> rhymingWords.contains(e.getKey()))
            .map(Map.Entry::getValue)
            .flatMap(Set::stream)
            .toList();

        List<Integer> lineIndices = IntStream.range(0, matchingLines.size())
            .boxed()
            .collect(Collectors.toList());

        Collections.shuffle(lineIndices);

        // Switch to a sequential loop so we don't have to evaluate every line
        for (int i : lineIndices)
        {
            Line line = matchingLines.get(i);
            String lastWord = line.words().getLast();
            boolean differentLastWord = previousLines.stream()
                .noneMatch(rhymingLine -> lastWord.equalsIgnoreCase(rhymingLine.words().getLast()));
            boolean matchesPreviousLine = previousLines.stream()
                .anyMatch(line::matches);

            if (!matchesPreviousLine && differentLastWord)
            {
                return line;
            }
        }
        return null;
    }

    /**
     * @return a map of (last word -> lines)
     */
    @Nonnull
    private Map<String, Set<Line>> _getLinesByMeter(@Nonnull Meter meter)
    {
        return m_linesByMeter.computeIfAbsent(meter, m ->
            m_lines.stream()
                .filter(line -> m.fitsLineMeter(line.getMeter()))
                .collect(Collectors.groupingBy(
                    line -> line.words().getLast().toUpperCase(),
                    Collectors.toSet()
                )));
    }
}
