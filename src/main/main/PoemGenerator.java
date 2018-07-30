package main;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import main.dictionaries.CMUDictionary;
import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.poetry.Poem;
import main.readers.SongLyricsReader;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jbutler
 * @since July 2018
 */
public class PoemGenerator
{
    @Nonnull
    private final IDictionary m_dictionary;
    @Nonnull
    private final List<Text> m_texts;
    @Nonnull
    private final Map<Integer, List<Line>> m_linesBySyllableCount;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        long t1 = System.currentTimeMillis();
        CMUDictionary dictionary = new CMUDictionary();

        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);
        long t2 = System.currentTimeMillis();
        System.out.println("Created dictionary in " + (t2 - t1) + " ms");
        Text songLyrics = songLyricsReader.readFile("songdata.csv");

        PoemGenerator poemGenerator = new PoemGenerator(dictionary, ImmutableList.of(
            // reader.readFile("hymnal.txt", "----------"),
            // reader.readFile("paradiselost.txt", "  BOOK I."),
            songLyrics
        ));
        long t3 = System.currentTimeMillis();
        System.out.println("Parsed lyrics CSV in " + (t3 - t2) + " ms");
        /*Poem poem = poemGenerator.generatePoem(
            ImmutableList.of(6, 6, 8, 6, 4, 6, 6, 8, 6, 4),
            ImmutableList.of('A', 'A', 'B', 'A', 'C', 'D', 'D', 'E', 'D', 'C'),
            5
        );*/
        Poem poem = poemGenerator.generatePoem(ImmutableList.of(9, 9, 3, 10), ImmutableList.of('A', 'A', 'B', 'C'), 20);
        long t4 = System.currentTimeMillis();
        System.out.println("Generated poem in " + (t4 - t3) + " ms");
        System.out.println();
        System.out.println(poem.toString());
    }

    public PoemGenerator(@Nonnull IDictionary dictionary, @Nonnull List<Text> texts)
    {
        m_dictionary = dictionary;
        m_texts = texts;
        m_linesBySyllableCount = new HashMap<>();
    }

    @Nonnull
    public Poem generatePoem(@Nonnull List<Integer> lineLengths, @Nonnull List<Character> rhymeScheme, int numStanzas)
    {
        Preconditions.checkArgument(lineLengths.size() > 0 && rhymeScheme.size() > 0);
        Preconditions.checkArgument(lineLengths.size() == rhymeScheme.size());
        ImmutableList.Builder<List<Line>> stanzas = new ImmutableList.Builder<>();
        IntStream.range(0, numStanzas)
            .parallel()
            .forEach(i ->
            {
                List<Line> stanza =_generateStanza(lineLengths, rhymeScheme);
                stanzas.add(stanza);
            });
        return new Poem(stanzas.build());
    }

    @Nonnull
    private List<Line> _generateStanza(@Nonnull List<Integer> lineLengths, @Nonnull List<Character> rhymeScheme)
    {
        List<Line> lines = new ArrayList<>();
        while (lines.size() < lineLengths.size())
        {
            lines.clear();
            for (int i = 0; i < lineLengths.size(); i++)
            {
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
                    lines.add(_generateFreshLine(lineLengths.get(i)));
                }
                else
                {
                    @CheckForNull Line rhymingLine = _generateRhymingLine(previousLines, lineLengths.get(i));
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
        Logging.debug("Generated stanza");
        return ImmutableList.copyOf(lines);
    }

    @Nonnull
    private Line _generateFreshLine(int numSyllables)
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

    @Nonnull
    private List<Line> _getLinesBySyllableCount(int numSyllables)
    {
        return m_linesBySyllableCount.computeIfAbsent(numSyllables, n ->
        {
            return m_texts.parallelStream()
                .map(Text::getLines)
                .flatMap(List::parallelStream)
                .filter(line -> line.getMeter().size() == numSyllables)
                .collect(Collectors.toList());
        });
    }

    @CheckForNull
    private Line _generateRhymingLine(@Nonnull List<Line> previousLines, int numSyllables)
    {
        Preconditions.checkArgument(!previousLines.isEmpty());

        Line firstLine = previousLines.get(0);
        String lastWordOfFirstLine = firstLine.getWords().get(firstLine.getWords().size() - 1);
        List<String> rhymingWords = ImmutableList.copyOf(m_dictionary.getRhymes(lastWordOfFirstLine));

        List<Line> lines = _getLinesBySyllableCount(numSyllables);

        List<Integer> lineIndices = IntStream.range(0, lines.size())
            .parallel()
            .boxed()
            .collect(Collectors.toList());

        Collections.shuffle(lineIndices);

        // Switch to a sequential loop so we don't have to evaluate every line
        for (int i : lineIndices)
        {
            Line line = lines.get(i);
            String lastWord = line.getWords().get(line.getWords().size() - 1);
            boolean rhymesWith = rhymingWords.contains(lastWord);
            boolean differentLastWord = previousLines.stream()
                .noneMatch(previousLine -> lastWord.equals(previousLine.getWords().get(previousLine.getWords().size() - 1)));
            boolean matchesPreviousLine = previousLines.stream()
                .anyMatch(line::equals);
            if (rhymesWith && !matchesPreviousLine && differentLastWord)
            {
                return line;
            }
        }
        return null;
    }
}
