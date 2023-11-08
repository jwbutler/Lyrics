package lyrics;

import javax.annotation.Nonnull;

import lyrics.dictionaries.CMUDictionary;
import lyrics.meter.Meter;
import lyrics.poetry.Line;
import lyrics.readers.SongLyricsReader;
import lyrics.texts.LineSupplier;
import lyrics.texts.PoetryLineSupplier;

import static lyrics.utils.Preconditions.checkArgument;

/**
 * @author jbutler
 * @since July 2018
 */
public final class MeterTester
{
    @Nonnull
    private final LineSupplier m_text;

    public static void main(String[] args)
    {
        CMUDictionary dictionary = new CMUDictionary();

        SongLyricsReader songLyricsReader = new SongLyricsReader(dictionary);
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
        checkArgument(!meter.isEmpty());
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
