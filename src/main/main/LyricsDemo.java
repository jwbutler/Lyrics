package main;

import com.google.common.collect.ImmutableList;
import main.dictionaries.CMUDictionary;
import main.poetry.Poem;
import main.readers.GutenbergReader;
import main.readers.SongLyricsReader;
import main.songs.Song;
import main.texts.PoetryLineSupplier;
import main.texts.ProseLineSupplier;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author jbutler
 * @since August 2018
 */
public class LyricsDemo
{
    private static int NUM_VERSES = 4;
    public static void main(String[] args) throws IOException, InterruptedException
    {
        CMUDictionary dictionary = new CMUDictionary();
        GutenbergReader gutenbergReader = new GutenbergReader(dictionary);
        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);

        //ProseLineSupplier prideAndPrejudice = gutenbergReader.readProseFile("prideandprejudice.txt", "Produced by Anonymous Volunteers");
        ProseLineSupplier critiqueOfPureReason = gutenbergReader.readProseFile("critiqueofpurereason.txt", "Produced by Charles Aldarondo and David Widger");
        PoetryLineSupplier songLyrics = songLyricsReader.readFile("songdata.csv");

        PoemGenerator poemGenerator = new PoemGenerator(dictionary, ImmutableList.of(
            //prideAndPrejudice,
            critiqueOfPureReason,
            songLyrics
        ));

        for (int i = 0; i < NUM_VERSES; i++)
        {
            writeSong(poemGenerator, Song.ORGAN_4_VERSE);
            System.out.println();
            writeSong(poemGenerator, Song.ORGAN_4_VERSE);
            System.out.println();
            writeSong(poemGenerator, Song.ORGAN_4_CHORUS);
            System.out.println();
            writeSong(poemGenerator, Song.ORGAN_4_CHORUS);
            if (i < (NUM_VERSES - 1))
            {
                System.out.println();
            }
        }
    }

    public static void writeSong(@Nonnull PoemGenerator poemGenerator, @Nonnull Song song) throws IOException
    {
        Poem poem = poemGenerator.generatePoem(song.getMeter(), song.getRhymeScheme(), 1);
        System.out.println(poem.toString());
    }
}
