package main;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import main.dictionaries.CMUDictionary;
import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.poetry.Poem;
import main.readers.SongLyricsReader;
import main.texts.IText;
import main.texts.PoetryText;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private final IText m_text;

    private static final List<List<Integer>> METERS = ImmutableList.of(
        ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
        ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
        ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1),
        ImmutableList.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1)
    );

    public static final List<Character> RHYME_SCHEME = ImmutableList.of('A', 'B', 'A', 'B');

    public static void main(String[] args) throws IOException, InterruptedException
    {
        long t1 = System.currentTimeMillis();
        CMUDictionary dictionary = new CMUDictionary();

        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);
        long t2 = System.currentTimeMillis();
        System.out.println("Created dictionary in " + (t2 - t1) + " ms");
        PoetryText songLyrics = songLyricsReader.readFile("songdata.csv");

        PoemGenerator poemGenerator = new PoemGenerator(dictionary, songLyrics);
        long t3 = System.currentTimeMillis();
        System.out.println("Parsed lyrics CSV in " + (t3 - t2) + " ms");
        Poem poem = poemGenerator.generatePoem(METERS, RHYME_SCHEME, 8);
        long t4 = System.currentTimeMillis();
        System.out.println("Generated poem in " + (t4 - t3) + " ms");
        System.out.println();
        System.out.println(poem.toString());
    }

    public PoemGenerator(@Nonnull IDictionary dictionary, @Nonnull IText text)
    {
        m_dictionary = dictionary;
        m_text = text;
    }

    @Nonnull
    public Poem generatePoem(@Nonnull List<List<Integer>> lineMeters, @Nonnull List<Character> rhymeScheme, int numStanzas)
    {
        Preconditions.checkArgument(lineMeters.size() > 0 && rhymeScheme.size() > 0);
        Preconditions.checkArgument(lineMeters.size() == rhymeScheme.size());
        ImmutableList.Builder<List<Line>> stanzas = new ImmutableList.Builder<>();
        IntStream.range(0, numStanzas)
            .parallel()
            .forEach(i ->
            {
                List<Line> stanza =_generateStanza(lineMeters, rhymeScheme);
                stanzas.add(stanza);
            });
        return new Poem(stanzas.build());
    }

    @Nonnull
    private List<Line> _generateStanza(@Nonnull List<List<Integer>> lineMeters, @Nonnull List<Character> rhymeScheme)
    {
        Logging.debug("Generating stanza ...");
        List<Line> lines = new ArrayList<>();
        while (lines.size() < lineMeters.size())
        {
            lines.clear();
            for (int i = 0; i < lineMeters.size(); i++)
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
                    lines.add(m_text.getLine(lineMeters.get(i)));
                }
                else
                {
                    @CheckForNull Line rhymingLine = m_text.getLine(previousLines, lineMeters.get(i));
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
        Logging.info("... Generated stanza");
        return ImmutableList.copyOf(lines);
    }
}
