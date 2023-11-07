package lyrics.readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import lyrics.Logging;
import lyrics.dictionaries.Dictionary;
import lyrics.poetry.Line;
import lyrics.texts.PoetryLineSupplier;
import lyrics.utils.FileUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

/**
 * Reader interface intended to parse this file:
 * https://www.kaggle.com/mousehead/songlyrics#songdata.csv
 *
 * @author jbutler
 * @since July 2018
 */
public class SongLyricsReader
{
    public static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder().setSkipHeaderRecord(true).build();
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
            CSVParser parser = new CSVParser(reader, CSV_FORMAT);
            var executor = Executors.newVirtualThreadPerTaskExecutor()
        )
        {
            long t1 = System.currentTimeMillis();
            var numErrors = new AtomicInteger(0);

            Set<Line> lines = executor.submit(() ->
                parser.getRecords()
                    .parallelStream()
                    .map(r -> r.get(3))
                    .flatMap(SongLyricsReader::_splitToLines)
                    .map(line ->
                    {
                        try
                        {
                            return Line.fromString(line, m_dictionary);
                        }
                        catch (Exception e)
                        {
                            Logging.debug(e.getMessage(), e);
                            numErrors.incrementAndGet();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet())
            ).get();

            long t2 = System.currentTimeMillis();
            Logging.info("SongLyricsReader - Read " + lines.size() + " lines in " + (t2-t1) + " ms with " + numErrors.get() + " errors");
            return new PoetryLineSupplier(m_dictionary, lines);
        }
        catch (IOException | InterruptedException | ExecutionException e)
        {
            // Not much point in continuing
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private static Stream<String> _splitToLines(@Nonnull String string)
    {
        return Arrays.stream(string.split("\\s*\n\\s*"));
    }
}
