package lyrics.texts;

import lyrics.RhymeMap;
import lyrics.Logging;
import lyrics.NoPronunciationException;
import lyrics.dictionaries.Dictionary;
import lyrics.linguistics.Pronunciation;
import lyrics.linguistics.Syllable;
import lyrics.meter.Meter;
import lyrics.poetry.Line;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static lyrics.utils.Preconditions.checkArgument;

/**
 * @author jwbutler
 * @since August 2018
 */
public final class ProseLineSupplier implements LineSupplier
{
    @Nonnull
    private final Dictionary m_dictionary;
    @Nonnull
    private final List<String> m_sentences;
    @Nonnull
    private final RhymeMap m_rhymeMap;
    /**
     * a map of (meter -> (last word -> lines))
     */
    @Nonnull
    private final Map<Meter, Map<String, List<Line>>> m_linesByMeter;

    public ProseLineSupplier(@Nonnull Dictionary dictionary, @Nonnull List<String> sentences)
    {
        m_dictionary = dictionary;
        m_sentences = sentences;
        m_rhymeMap = RhymeMap.create(dictionary);
        m_linesByMeter = new HashMap<>();
    }

    @Override
    @Nonnull
    public Line getLine(@Nonnull Meter meter)
    {
        if (!m_linesByMeter.containsKey(meter))
        {
            m_linesByMeter.put(meter, _computeLinesForMeter(meter));
        }
        List<Line> lines = m_linesByMeter.getOrDefault(meter, Collections.emptyMap())
            .values()
            .stream()
            .flatMap(List::stream)
            .toList();

        if (lines.isEmpty())
        {
            // bail out, we're not gonna finish this poem
            throw new RuntimeException("No lines found for meter " + meter);
        }

        Random RNG = ThreadLocalRandom.current();
        int index = RNG.nextInt(lines.size());

        return lines.get(index);
    }

    /**
     * TODO - massive duplication with PoetryLineSupplier.
     * Brainstorm ways to move some of this to a shared class.
     */
    @Override
    @CheckForNull
    public Line getLine(@Nonnull List<Line> previousLines, @Nonnull Meter meter)
    {
        checkArgument(!previousLines.isEmpty());

        if (!m_linesByMeter.containsKey(meter))
        {
            m_linesByMeter.put(meter, _computeLinesForMeter(meter));
        }

        Line firstLine = previousLines.get(0);
        String lastWordOfFirstLine = firstLine.words().get(firstLine.words().size() - 1);
        Set<String> rhymingWords = m_rhymeMap.getRhymes(lastWordOfFirstLine);

        List<Line> matchingLines = m_linesByMeter.getOrDefault(meter, Collections.emptyMap())
            .entrySet()
            .stream()
            .filter(e -> rhymingWords.contains(e.getKey()))
            .map(Map.Entry::getValue)
            .flatMap(List::stream)
            .toList();

        List<Integer> lineIndices = IntStream.range(0, matchingLines.size())
            .boxed()
            .collect(Collectors.toList());

        Collections.shuffle(lineIndices);

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
     * @return a map of (last word in the line -> line)
     */
    @Nonnull
    private Map<String, List<Line>> _computeLinesForMeter(@Nonnull Meter meter)
    {
        return m_sentences.stream()
            .map(sentence -> _computeLinesForSentenceAndMeter(sentence, meter))
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(line -> line.words().getLast()));
    }

    /**
     * @param sentence Assumed to be non-empty
     */
    @Nonnull
    private List<Line> _computeLinesForSentenceAndMeter(@Nonnull String sentence, @Nonnull Meter meter)
    {
        List<Line> lines = new ArrayList<>();
        try
        {
            Line sentenceAsLine = Line.fromString(sentence, m_dictionary);

            // compute line starting from the first word
            for (int i = 0; i < sentenceAsLine.words().size(); i++)
            {
                for (int j = i + 1; j < sentenceAsLine.words().size(); j++)
                {
                    // subList's second parameter is exclusive, so this is up to and including j
                    List<String> words = sentenceAsLine.words()
                        .subList(i, j + 1);

                    // if any words aren't in the dictionary, give up
                    if (words.stream().anyMatch(w -> m_dictionary.getPronunciations(w).isEmpty()))
                    {
                        break;
                    }

                    List<Syllable> syllables = words.stream()
                        .map(m_dictionary::getPronunciations)
                        .map(set -> set.iterator().next())
                        .map(Pronunciation::syllables)
                        .flatMap(List::stream)
                        .toList();
                    Meter lineMeter = Meter.forSyllables(syllables);

                    if (meter.fitsLineMeter(lineMeter))
                    {
                        lines.add(Line.fromString(String.join(" ", words), m_dictionary));
                        break;
                    }
                }
            }
        }
        catch (NoPronunciationException e)
        {
            // just skip it and continue
        }
        catch (RuntimeException e)
        {
            Logging.error("Error computing lines: ", e);
        }
        return lines;
    }
}
