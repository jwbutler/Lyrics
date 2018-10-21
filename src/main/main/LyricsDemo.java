package main;

import com.google.common.collect.ImmutableList;
import main.dictionaries.CMUDictionary;
import main.poetry.Poem;
import main.readers.GutenbergReader;
import main.readers.SongLyricsReader;
import main.readers.UrbanDictionaryReader;
import main.songs.SongPattern;
import main.songs.StanzaPattern;
import main.texts.GutenbergText;
import main.texts.ILineSupplier;
import main.texts.PoetryLineSupplier;
import main.texts.ProseLineSupplier;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author jbutler
 * @since August 2018
 */
public class LyricsDemo
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        CMUDictionary dictionary = new CMUDictionary();

        //UrbanDictionaryReader urbanDictionaryReader = new UrbanDictionaryReader(dictionary);
        //GutenbergReader gutenbergReader = new GutenbergReader(dictionary);
        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);

        ILineSupplier songLyrics = songLyricsReader.readFile("songdata.csv");
        //ILineSupplier urbanDictionary = urbanDictionaryReader.readFileAsPoetry("urbandict-word-def.csv");

        PoemGenerator poemGenerator = new PoemGenerator(dictionary, ImmutableList.of(
            //GutenbergText.ARISTOTLE_POETICS.getLineSupplier(gutenbergReader),
            //GutenbergText.KANT_CRITIQUE_OF_PURE_REASON.getLineSupplier(gutenbergReader),
            songLyrics
            //urbanDictionary
        ));

        _writeSong(poemGenerator, new SongPattern(ImmutableList.of(
            StanzaPattern.JESUS_KING_OF_GLORY
        ), 100));
    }

    private static void _writeSong(@Nonnull PoemGenerator poemGenerator, @Nonnull SongPattern songPattern)
    {
        IntStream.range(0, songPattern.getNumVerses()).parallel().forEach(i ->
        {
            ImmutableList.Builder<Poem> builder = new ImmutableList.Builder<>();
            // these need to be inserted in order
            for (int j = 0; j < songPattern.getStanzaPatterns().size(); j++)
            {
                StanzaPattern stanza = songPattern.getStanzaPatterns().get(j);
                Poem poem = poemGenerator.generatePoem(stanza.getMeter(), stanza.getRhymeScheme(), 1);
                builder.add(poem);
            }
            List<Poem> stanzas = builder.build();
            synchronized (LyricsDemo.class)
            {
                for (Poem poem : stanzas)
                {
                    System.out.println(poem);
                    System.out.println();
                }
            }
        });
    }
}
