package lyrics;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import lyrics.dictionaries.CMUDictionary;
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
            //return new SongPattern(List.of(StanzaPatterns.CAVEMAN), 100);
            return new SongPattern(List.of(StanzaPatterns.SPACE_DAGGER), 100);
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
        return new SongPattern(List.of(stanzaPattern), 20);
    }

    private static void _writeSong(@Nonnull PoemGenerator poemGenerator, @Nonnull SongPattern songPattern)
    {
        for (int i = 0; i < songPattern.numVerses(); i++)
        {
            List<Poem> stanzas = new ArrayList<>();
            // these need to be inserted in order
            for (int j = 0; j < songPattern.stanzaPatterns().size(); j++)
            {
                StanzaPattern stanzaPattern = songPattern.stanzaPatterns().get(j);
                Poem stanza = poemGenerator.generatePoem(stanzaPattern.meters(), stanzaPattern.rhymeScheme(), 1);
                stanzas.add(stanza);
            }

            for (Poem stanza : stanzas)
            {
                System.out.println(stanza);
                System.out.println();
            }
        }
    }
}
