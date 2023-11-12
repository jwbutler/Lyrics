package lyrics;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import io.javalin.Javalin;
import lyrics.dictionaries.CMUDictionary;
import lyrics.dictionaries.Dictionary;
import lyrics.meter.Meter;
import lyrics.poetry.Poem;
import lyrics.readers.SongLyricsReader;
import lyrics.songs.SongPattern;
import lyrics.songs.StanzaPattern;
import lyrics.songs.StanzaPatterns;
import lyrics.texts.LineSupplier;

/**
 * Main entry point.
 *
 * @author jbutler
 * @since August 2018
 */
public final class LyricsApp
{
    private static final SongPattern DEFAULT_PATTERN = new SongPattern(List.of(StanzaPatterns.SPACE_DAGGER), 100);
    public static final int PORT = 7070;

    @Nonnull
    private final Dictionary dictionary;
    @Nonnull
    private final PoemGenerator poemGenerator;
    
    public static void main(String[] args)
    {
        new LyricsApp().start();
    }
    
    public LyricsApp()
    {
        dictionary = new CMUDictionary();

        //UrbanDictionaryReader urbanDictionaryReader = new UrbanDictionaryReader(dictionary);
        //GutenbergReader gutenbergReader = new GutenbergReader(dictionary);
        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);

        LineSupplier songLyrics = songLyricsReader.readFile("songdata.csv");
        //ILineSupplier urbanDictionary = urbanDictionaryReader.readFileAsPoetry("urbandict-word-def.csv");

        poemGenerator = new PoemGenerator(List.of(
            //GutenbergText.ARISTOTLE_POETICS.getLineSupplier(gutenbergReader),
            //GutenbergText.KANT_CRITIQUE_OF_PURE_REASON.getLineSupplier(gutenbergReader),
            songLyrics
            //urbanDictionary
        ));
    }
    
    public void start()
    {
        try (var app = Javalin.create()
            .get("/lyrics", context ->
            {
                var patternArg = context.queryParam("pattern");
                var songPattern = (patternArg != null)
                    ? parsePattern(patternArg)
                    : DEFAULT_PATTERN;
                var song = this.writeSong(songPattern);
                context.result(song);
            }))
        {
            app.start(PORT);
            while (true);
        }
    }

    /**
     * @param arg A string in the form 10101010A10101010B10101010A10101010B
     */
    @Nonnull
    private static SongPattern parsePattern(@Nonnull String arg)
    {
        List<Meter> meters = new ArrayList<>();
        List<Character> rhymes = new ArrayList<>();
        ArrayList<Integer> meter = new ArrayList<>();

        boolean lastCharWasNumber = false;

        for (int index = 0; index < arg.length(); index++)
        {
            char c = arg.charAt(index);
            if (c >= '0' && c <= '1')
            {
                int i = c - '0';
                meter.add(i);
                lastCharWasNumber = true;
            }
            else if (Character.toUpperCase(c) >= 'A' && Character.toUpperCase(c) <= 'Z')
            {
                if (lastCharWasNumber)
                {
                    meters.add(Meter.of(meter));
                    meter.clear();
                    rhymes.add(c);
                    lastCharWasNumber = false;
                }
                else
                {
                    throw new IllegalArgumentException();
                }
            }
        }

        StanzaPattern stanzaPattern = new StanzaPattern(meters, rhymes);
        return new SongPattern(List.of(stanzaPattern), 20);
    }

    @Nonnull
    public String writeSong(@Nonnull SongPattern songPattern)
    {
        var lines = new ArrayList<String>();
        for (int i = 0; i < songPattern.numVerses(); i++)
        {
            List<Poem> poems = new ArrayList<>();
            // these need to be inserted in order
            for (int j = 0; j < songPattern.stanzaPatterns().size(); j++)
            {
                StanzaPattern stanzaPattern = songPattern.stanzaPatterns().get(j);
                Poem poem = poemGenerator.generatePoem(stanzaPattern.meters(), stanzaPattern.rhymeScheme(), 1);
                poems.add(poem);
            }

            for (Poem poem : poems)
            {
                for (var stanza : poem.stanzas())
                {
                    for (var line : stanza)
                    {
                        lines.add(line.toString());
                    }
                    lines.add("");
                }
                lines.add("");
            }
        }
        return String.join("\n", lines);
    }
}
