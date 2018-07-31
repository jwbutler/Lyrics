package main.texts;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import main.RhymeMap;
import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.utils.MeterUtils;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
public class PoetryText implements IText
{
    @Nonnull
    private final IDictionary m_dictionary;
    @Nonnull
    private final List<Line> m_lines;
    @Nonnull
    private final RhymeMap m_rhymeMap;
    @Nonnull
    private final Map<List<Integer>, Map<String, List<Line>>> m_linesByMeter;

    public PoetryText(@Nonnull IDictionary dictionary, @Nonnull List<Line> lines)
    {
        m_dictionary = dictionary;
        m_lines = ImmutableList.copyOf(lines);
        m_rhymeMap = new RhymeMap(m_dictionary);
        m_linesByMeter = new ConcurrentHashMap<>();
    }

    @Nonnull
    @Override
    public Line getLine(@Nonnull List<Integer> meter)
    {
        Map<String, List<Line>> matchingLineMap = _getLinesByMeter(meter);

        if (matchingLineMap.isEmpty())
        {
            throw new IllegalStateException("No lines found for meter " + meter);
        }

        List<Line> lines = matchingLineMap.values()
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        ThreadLocalRandom RNG = ThreadLocalRandom.current();
        int index = RNG.nextInt(lines.size());
        return lines.get(index);
    }

    @Override
    public Line getLine(@Nonnull List<Line> rhymingLines, @Nonnull List<Integer> meter)
    {
        Preconditions.checkArgument(!rhymingLines.isEmpty());

        Line firstLine = rhymingLines.get(0);
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
            boolean rhymesWith = rhymingWords.contains(lastWord.toUpperCase());
            boolean differentLastWord = rhymingLines.parallelStream()
                .noneMatch(rhymingLine -> lastWord.equalsIgnoreCase(rhymingLine.getWords().get(rhymingLine.getWords().size() - 1)));
            boolean matchesPreviousLine = rhymingLines.parallelStream()
                .anyMatch(line::equals);

            if (rhymesWith && !matchesPreviousLine && differentLastWord)
            {
                return line;
            }
        }
        return null;
    }

    /**
     * @return a map of (last word in the line -> line)
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
