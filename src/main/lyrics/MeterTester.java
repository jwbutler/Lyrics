package lyrics;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lyrics.dictionaries.CMUDictionary;
import lyrics.dictionaries.IDictionary;
import lyrics.poetry.Line;
import lyrics.readers.SongLyricsReader;
import lyrics.texts.ILineSupplier;
import lyrics.texts.PoetryLineSupplier;
import lyrics.utils.MeterUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public class MeterTester
{
    @Nonnull
    private final IDictionary m_dictionary;
    @Nonnull
    private final ILineSupplier m_text;

    public static void main(String[] args) throws IOException
    {
        long t1 = System.currentTimeMillis();
        CMUDictionary dictionary = new CMUDictionary();

        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);
        long t2 = System.currentTimeMillis();
        System.out.println("Created dictionary in " + (t2 - t1) + " ms");
        PoetryLineSupplier songLyrics = songLyricsReader.readFile("songdata.csv");

        MeterTester meterTester = new MeterTester(dictionary, songLyrics);
        meterTester.doTest(ImmutableList.of(0,1,0,1,0,1,0,1,0,1));
    }

    public MeterTester(@Nonnull IDictionary dictionary, @Nonnull ILineSupplier text)
    {
        m_dictionary = dictionary;
        m_text = text;
    }

    public void doTest(@Nonnull List<Integer> meter)
    {
        Preconditions.checkArgument(!meter.isEmpty());
        for (int i = 0; i < 10; i++)
        {
            Line line;
            do
            {
                line = m_text.getLine(meter);
            }
            while (!MeterUtils.fitsMeter(meter, line.getMeter()));

            System.out.printf("%-50s => %s\n", line, line.getMeter());
        }
    }
}
