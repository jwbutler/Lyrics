package lyrics;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import lyrics.dictionaries.CMUDictionary;
import lyrics.meter.Meter;
import lyrics.poetry.Line;
import lyrics.readers.SongLyricsReader;
import lyrics.texts.LineSupplier;
import lyrics.texts.PoetryLineSupplier;

/**
 * @author jbutler
 * @since July 2018
 */
public class MeterTester
{
    @Nonnull
    private final LineSupplier m_text;

    public static void main(String[] args)
    {
        long t1 = System.currentTimeMillis();
        CMUDictionary dictionary = new CMUDictionary();

        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);
        long t2 = System.currentTimeMillis();
        System.out.println("Created dictionary in " + (t2 - t1) + " ms");
        PoetryLineSupplier songLyrics = songLyricsReader.readFile("songdata.csv");

        MeterTester meterTester = new MeterTester(songLyrics);
        meterTester.doTest(Meter.of(0, 1, 0, 1, 0, 1, 0, 1, 0, 1));
    }

    public MeterTester(@Nonnull LineSupplier text)
    {
        m_text = text;
    }

    public void doTest(@Nonnull Meter meter)
    {
        Preconditions.checkArgument(!meter.isEmpty());
        for (int i = 0; i < 10; i++)
        {
            Line line;
            do
            {
                line = m_text.getLine(meter);
            }
            while (!meter.fitsLineMeter(line.getMeter()));

            System.out.printf("%-50s => %s\n", line, line.getMeter());
        }
    }
}
