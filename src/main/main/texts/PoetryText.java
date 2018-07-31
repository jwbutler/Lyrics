package main.texts;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.utils.MeterUtils;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.HashMap;
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
    private final Map<List<Integer>, List<Line>> m_linesByMeter;

    public PoetryText(@Nonnull IDictionary dictionary, @Nonnull List<Line> lines)
    {
        m_dictionary = dictionary;
        m_lines = ImmutableList.copyOf(lines);
        m_linesByMeter = new ConcurrentHashMap<>();
    }

    @Nonnull
    @Override
    public Line getLine(@Nonnull List<Integer> meter)
    {
        List<Line> matchingLines = _getLinesByMeter(meter);

        if (matchingLines.isEmpty())
        {
            throw new IllegalStateException("No lines found for meter " + meter);
        }

        ThreadLocalRandom RNG = ThreadLocalRandom.current();
        int index = RNG.nextInt(matchingLines.size());
        return matchingLines.get(index);
    }

    @Override
    public Line getLine(@Nonnull List<Line> rhymingLines, @Nonnull List<Integer> meter)
    {
        Preconditions.checkArgument(!rhymingLines.isEmpty());

        Line firstLine = rhymingLines.get(0);
        String lastWordOfFirstLine = firstLine.getWords().get(firstLine.getWords().size() - 1);
        Set<String> rhymingWords = m_dictionary.getRhymes(lastWordOfFirstLine);

        if (rhymingWords.isEmpty())
        {
            return null;
        }

        List<Line> lines = _getLinesByMeter(meter);

        List<Integer> lineIndices = IntStream.range(0, lines.size())
            .parallel()
            .boxed().collect(Collectors.toList());

        Collections.shuffle(lineIndices);

        // Switch to a sequential loop so we don't have to evaluate every line
        for (int i : lineIndices)
        {
            Line line = lines.get(i);
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

    @Nonnull
    private List<Line> _getLinesByMeter(@Nonnull List<Integer> meter)
    {
        return m_linesByMeter.computeIfAbsent(meter, m ->
        {
            return m_lines.parallelStream()
                .distinct()
                .filter(line -> MeterUtils.fitsMeter(m, line.getMeter()))
                .collect(Collectors.toList());
        });
    }
}
