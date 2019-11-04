package lyrics.readers;

import lyrics.Logging;
import lyrics.texts.PoetryLineSupplier;
import lyrics.dictionaries.Dictionary;
import lyrics.poetry.Line;
import lyrics.utils.FileUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Reader interface intended to parse this file:
 * https://www.kaggle.com/mousehead/songlyrics#songdata.csv
 *
 * @author jbutler
 * @since July 2018
 */
public class SongLyricsReader
{
    @Nonnull
    private final Dictionary m_dictionary;

    public SongLyricsReader(@Nonnull Dictionary dictionary)
    {
        m_dictionary = dictionary;
    }

    @Nonnull
    public PoetryLineSupplier readFile(@Nonnull String filename)
    {
        System.out.println("SongLyricsReader - Reading song lyrics...");
        try (
            BufferedReader reader = FileUtils.getBufferedReader(filename);
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withSkipHeaderRecord())
        )
        {
            long t1 = System.currentTimeMillis();
            AtomicInteger numErrors = new AtomicInteger(0);

            List<Line> lines = parser.getRecords()
                .parallelStream()
                .map(r -> r.get(3))
                .map(lyrics -> lyrics.split("\\s*\n\\s*"))
                .flatMap(Arrays::stream)
                .parallel()
                .map(line ->
                {
                    try
                    {
                        return new Line(line, m_dictionary);
                    }
                    catch (Exception e)
                    {
                        Logging.debug(e.getMessage(), e);
                        numErrors.incrementAndGet();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

            long t2 = System.currentTimeMillis();
            Logging.info("SongLyricsReader - Read " + lines.size() + " lines in " + (t2-t1) + " ms with " + numErrors.get() + " errors");
            return new PoetryLineSupplier(m_dictionary, lines);
        }
        catch (IOException e)
        {
            // Not much point in continuing
            throw new RuntimeException(e);
        }
    }
}
