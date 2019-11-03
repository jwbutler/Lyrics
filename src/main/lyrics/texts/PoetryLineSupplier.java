package lyrics.texts;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lyrics.RhymeMap;
import lyrics.dictionaries.IDictionary;
import lyrics.poetry.Line;
import lyrics.utils.MeterUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A text consisting of a collection of discrete lines.
 *
 * @author jbutler
 * @since July 2018
 */
@Immutable
public class PoetryLineSupplier implements ILineSupplier
{
    @Nonnull
    private final IDictionary m_dictionary;
    @Nonnull
    private final List<Line> m_lines;
    @Nonnull
    private final RhymeMap m_rhymeMap;
    /**
     * a map of (meter -> (last word -> lines))
     */
    @Nonnull
    private final Map<List<Integer>, Map<String, List<Line>>> m_linesByMeter;

    public PoetryLineSupplier(@Nonnull IDictionary dictionary, @Nonnull List<Line> lines)
    {
        m_dictionary = dictionary;
        m_lines = ImmutableList.copyOf(lines);
        m_rhymeMap = new RhymeMap(m_dictionary);
        m_linesByMeter = new ConcurrentHashMap<>();
    }

    @Override
    @CheckForNull
    public Line getLine(@Nonnull List<Integer> meter)
    {
        Map<String, List<Line>> matchingLineMap = _getLinesByMeter(meter);

        if (matchingLineMap.isEmpty())
        {
            throw new IllegalStateException("No lines found for meter " + meter);
        }

        List<Line> lines = matchingLineMap.values()
            .parallelStream()
            .flatMap(List::parallelStream)
            .collect(Collectors.toList());

        if (lines.isEmpty())
        {
            return null;
        }

        Random RNG = ThreadLocalRandom.current();
        int index = RNG.nextInt(lines.size());
        return lines.get(index);
    }

    @Override
    @CheckForNull
    public Line getLine(@Nonnull List<Line> previousLines, @Nonnull List<Integer> meter)
    {
        Preconditions.checkArgument(!previousLines.isEmpty());

        Line firstLine = previousLines.get(0);
        String lastWordOfFirstLine = firstLine.getWords().get(firstLine.getWords().size() - 1);
        Set<String> rhymingWords = m_rhymeMap.getRhymes(lastWordOfFirstLine);

        if (rhymingWords.isEmpty())
        {
            return null;
        }

        Map<String, List<Line>> lines = _getLinesByMeter(meter);

        List<Line> matchingLines = lines.entrySet()
            .parallelStream()
            .filter(e -> rhymingWords.contains(e.getKey()))
            .map(Map.Entry::getValue)
            .flatMap(List::stream)
            .collect(Collectors.toList());

        List<Integer> lineIndices = IntStream.range(0, matchingLines.size())
            .parallel()
            .boxed()
            .collect(Collectors.toList());

        Collections.shuffle(lineIndices);

        // Switch to a sequential loop so we don't have to evaluate every line
        for (int i : lineIndices)
        {
            Line line = matchingLines.get(i);
            String lastWord = line.getWords().get(line.getWords().size() - 1);
            boolean differentLastWord = previousLines.parallelStream()
                .noneMatch(rhymingLine -> lastWord.equalsIgnoreCase(rhymingLine.getWords().get(rhymingLine.getWords().size() - 1)));
            boolean matchesPreviousLine = previousLines.parallelStream()
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
    private Map<String, List<Line>> _getLinesByMeter(@Nonnull List<Integer> meter)
    {
        return m_linesByMeter.computeIfAbsent(meter, m ->
        {
            return m_lines.parallelStream()
                .filter(line -> MeterUtils.fitsMeter(m, line.getMeter()))
                .collect(Collectors.groupingBy(
                    line -> line.getWords().get(line.getWords().size() - 1).toUpperCase()
                ));
        });
    }
}
