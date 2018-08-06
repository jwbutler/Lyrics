package main.texts;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import main.Logging;
import main.RhymeMap;
import main.dictionaries.IDictionary;
import main.linguistics.Pronunciation;
import main.linguistics.Syllable;
import main.poetry.Line;
import main.utils.MeterUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
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
 * @author jbutler
 * @since August 2018
 */
public class ProseLineSupplier implements ILineSupplier
{
    @Nonnull
    private final IDictionary m_dictionary;
    @Nonnull
    private final List<String> m_sentences;
    @Nonnull
    private final RhymeMap m_rhymeMap;
    /**
     * a map of (meter -> (last word -> lines))
     */
    @Nonnull
    private final Map<List<Integer>, Map<String, List<Line>>> m_linesByMeter;

    public ProseLineSupplier(@Nonnull IDictionary dictionary, @Nonnull List<String> sentences)
    {
        m_dictionary = dictionary;
        m_sentences = sentences;
        m_rhymeMap = new RhymeMap(m_dictionary);
        m_linesByMeter = new ConcurrentHashMap<>();
    }

    @Override
    @Nonnull
    public Line getLine(@Nonnull List<Integer> meter)
    {
        if (!m_linesByMeter.containsKey(meter))
        {
            m_linesByMeter.put(meter, _computeLinesForMeter(meter));
        }
        List<Line> lines = m_linesByMeter.getOrDefault(meter, Collections.emptyMap())
            .values()
            .parallelStream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        if (lines.isEmpty())
        {
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
    public Line getLine(@Nonnull List<Line> previousLines, @Nonnull List<Integer> meter)
    {
        Preconditions.checkArgument(!previousLines.isEmpty());

        if (!m_linesByMeter.containsKey(meter))
        {
            m_linesByMeter.put(meter, _computeLinesForMeter(meter));
        }

        Line firstLine = previousLines.get(0);
        String lastWordOfFirstLine = firstLine.getWords().get(firstLine.getWords().size() - 1);
        Set<String> rhymingWords = m_rhymeMap.getRhymes(lastWordOfFirstLine);

        List<Line> matchingLines = m_linesByMeter.get(meter)
            .entrySet()
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
                .anyMatch(line::equals);

            if (!matchesPreviousLine && differentLastWord)
            {
                return line;
            }
        }
        return null;
    }

    @Nonnull
    private Map<String, List<Line>> _computeLinesForMeter(@Nonnull List<Integer> meter)
    {
        return m_sentences.parallelStream()
            .map(sentence -> _computeLinesForSentenceAndMeter(sentence, meter))
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(line -> line.getWords().get(line.getWords().size() - 1).toUpperCase()));
    }

    /**
     * @param sentence Assumed to be non-empty
     */
    @Nonnull
    private List<Line> _computeLinesForSentenceAndMeter(@Nonnull String sentence, @Nonnull List<Integer> meter)
    {
        ImmutableList.Builder<Line> builder = new ImmutableList.Builder<>();
        try
        {
            Line sentenceAsLine = new Line(sentence, m_dictionary);

            // compute line starting from the first word
            for (int i = 0; i < sentenceAsLine.getWords().size(); i++)
            {
                // subList's second parameter is exclusive, so this is up to and including i
                List<String> words = sentenceAsLine.getWords().subList(0, i + 1);

                // if any words aren't in the dictionary, give up
                if (words.stream().anyMatch(w -> m_dictionary.getPronunciations(w).isEmpty()))
                {
                    break;
                }

                List<Syllable> syllables = words.stream()
                    .map(m_dictionary::getPronunciations)
                    .map(list -> list.get(0))
                    .map(Pronunciation::getSyllables)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

                if (MeterUtils.fitsMeter(meter, MeterUtils.getMeterForSyllables(syllables)))
                {
                    builder.add(new Line(words.stream()
                        .collect(Collectors.joining(" ")), m_dictionary));
                    break;
                }
            }

            // compute line ending on the last word
            for (int i = sentenceAsLine.getWords().size() - 1; i >= 0; i--)
            {
                // subList's second parameter is exclusive, so this goes up to the last element
                List<String> words = sentenceAsLine.getWords().subList(i, sentenceAsLine.getWords().size());

                // if any words aren't in the dictionary, give up
                if (words.stream().anyMatch(w -> m_dictionary.getPronunciations(w).isEmpty()))
                {
                    break;
                }

                List<Syllable> syllables = words.stream()
                    .map(m_dictionary::getPronunciations)
                    .map(list -> list.get(0))
                    .map(Pronunciation::getSyllables)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

                if (MeterUtils.fitsMeter(meter, MeterUtils.getMeterForSyllables(syllables)))
                {
                    builder.add(new Line(words.stream().collect(Collectors.joining(" ")), m_dictionary));
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Logging.debug("Error computing lines: ", e);
        }
        return builder.build();
    }
}
