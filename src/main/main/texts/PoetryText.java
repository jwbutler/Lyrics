package main.texts;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import main.dictionaries.IDictionary;
import main.poetry.Line;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final Map<Integer, List<Line>> m_linesBySyllableCount;

    public PoetryText(@Nonnull IDictionary dictionary, @Nonnull List<Line> lines)
    {
        m_dictionary = dictionary;
        m_lines = ImmutableList.copyOf(lines);
        m_linesBySyllableCount = new HashMap<>();
    }

    @Nonnull
    @Override
    public Line getLine(int numSyllables)
    {
        List<Line> matchingLines = _getLinesBySyllableCount(numSyllables);

        if (matchingLines.isEmpty())
        {
            throw new IllegalStateException("No lines found with length " + numSyllables);
        }

        ThreadLocalRandom RNG = ThreadLocalRandom.current();
        int index = RNG.nextInt(matchingLines.size());
        return matchingLines.get(index);
    }

    @Override
    public Line getLine(@Nonnull List<Line> rhymingLines, int numSyllables)
    {
        Preconditions.checkArgument(!rhymingLines.isEmpty());

        Line firstLine = rhymingLines.get(0);
        String lastWordOfFirstLine = firstLine.getWords().get(firstLine.getWords().size() - 1);
        List<String> rhymingWords = ImmutableList.copyOf(m_dictionary.getRhymes(lastWordOfFirstLine));

        List<Line> lines = _getLinesBySyllableCount(numSyllables);

        List<Integer> lineIndices = IntStream.range(0, lines.size()).parallel().boxed().collect(Collectors.toList());

        Collections.shuffle(lineIndices);

        // Switch to a sequential loop so we don't have to evaluate every line
        for (int i : lineIndices)
        {
            Line line = lines.get(i);
            String lastWord = line.getWords().get(line.getWords().size() - 1);
            boolean rhymesWith = rhymingWords.contains(lastWord);
            boolean differentLastWord = rhymingLines.stream()
                .noneMatch(rhymingLine -> lastWord.equalsIgnoreCase(rhymingLine.getWords().get(rhymingLine.getWords().size() - 1)));
            boolean matchesPreviousLine = rhymingLines.stream()
                .anyMatch(line::equals);

            if (rhymesWith && !matchesPreviousLine && differentLastWord)
            {
                return line;
            }
        }
        return null;
    }

    @Nonnull
    private List<Line> _getLinesBySyllableCount(int numSyllables)
    {
        return m_linesBySyllableCount.computeIfAbsent(numSyllables, n ->
        {
            return m_lines.parallelStream()
                .filter(line -> line.getMeter().size() == numSyllables)
                .collect(Collectors.toList());
        });
    }
}
