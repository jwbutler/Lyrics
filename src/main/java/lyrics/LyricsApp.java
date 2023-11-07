package lyrics;

import lyrics.dictionaries.CMUDictionary;
import lyrics.meter.Meter;
import lyrics.poetry.Poem;
import lyrics.readers.SongLyricsReader;
import lyrics.songs.SongPattern;
import lyrics.songs.StanzaPattern;
import lyrics.texts.LineSupplier;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Main entry point.
 *
 * @author jbutler
 * @since August 2018
 */
public class LyricsApp
{
    public static void main(String[] args)
    {
        SongPattern songPattern = _readSongPattern(args);

        CMUDictionary dictionary = new CMUDictionary();

        //UrbanDictionaryReader urbanDictionaryReader = new UrbanDictionaryReader(dictionary);
        //GutenbergReader gutenbergReader = new GutenbergReader(dictionary);
        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);

        LineSupplier songLyrics = songLyricsReader.readFile("songdata.csv");
        //ILineSupplier urbanDictionary = urbanDictionaryReader.readFileAsPoetry("urbandict-word-def.csv");

        PoemGenerator poemGenerator = new PoemGenerator(List.of(
            //GutenbergText.ARISTOTLE_POETICS.getLineSupplier(gutenbergReader),
            //GutenbergText.KANT_CRITIQUE_OF_PURE_REASON.getLineSupplier(gutenbergReader),
            songLyrics
            //urbanDictionary
        ));

        _writeSong(poemGenerator, songPattern);
    }

    @Nonnull
    private static SongPattern _readSongPattern(String[] args)
    {
        if (args.length == 0)
        {
            //return new SongPattern(List.of(StanzaPattern.CAVEMAN), 100);
            return new SongPattern(List.of(StanzaPattern.SPACE_DAGGER), 100);
        }
        String pattern = args[0];

        List<Meter> meters = new ArrayList<>();
        List<Character> rhymes = new ArrayList<>();
        ArrayList<Integer> meter = new ArrayList<>();

        boolean lastCharWasNumber = false;

        for (int index = 0; index < pattern.length(); index++)
        {
            char c = pattern.charAt(index);
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
        return new SongPattern(stanzaPattern, 20);
    }

    private static void _writeSong(@Nonnull PoemGenerator poemGenerator, @Nonnull SongPattern songPattern)
    {
        IntStream.range(0, songPattern.getNumVerses()).forEach(i ->
        {
            List<Poem> stanzas = new ArrayList<>();
            // these need to be inserted in order
            for (int j = 0; j < songPattern.getStanzaPatterns().size(); j++)
            {
                StanzaPattern stanza = songPattern.getStanzaPatterns().get(j);
                Poem poem = poemGenerator.generatePoem(stanza.getMeters(), stanza.getRhymeScheme(), 1);
                stanzas.add(poem);
            }

            for (Poem poem : stanzas)
            {
                System.out.println(poem);
                System.out.println();
            }
        });
    }
}
