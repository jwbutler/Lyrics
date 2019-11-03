package lyrics.readers;

import lyrics.Logging;
import lyrics.dictionaries.IDictionary;
import lyrics.poetry.Line;
import lyrics.texts.PoetryLineSupplier;
import lyrics.texts.ProseLineSupplier;
import lyrics.utils.FileUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * https://www.kaggle.com/therohk/urban-dictionary-words-dataset
 *
 * @author jbutler
 * @since August 2018
 */
public class UrbanDictionaryReader
{
    public static final int DEFINITION_COLUMN_INDEX = 5;
    public static final int MAX_LINES = 200_000;
    @Nonnull
    private final IDictionary m_dictionary;

    public UrbanDictionaryReader(@Nonnull IDictionary dictionary)
    {
        m_dictionary = dictionary;
    }

    @Nonnull
    public ProseLineSupplier readFile(@Nonnull String filename)
    {
        CSVFormat csvFormat = CSVFormat.DEFAULT
            .withSkipHeaderRecord()
            .withQuoteMode(QuoteMode.NONE);
        try
        {
            long t1 = System.currentTimeMillis();
            BufferedReader reader = FileUtils.getBufferedReader(filename);
            CSVParser parser = new CSVParser(reader, csvFormat); // ignore quote characters, shit's fucked
            List<CSVRecord> records = parser.getRecords();

            List<String> lines = records.parallelStream()
                .map(r -> r.get(DEFINITION_COLUMN_INDEX))
                .map(lyrics -> lyrics.split("\\s*;;\\s*"))
                .flatMap(Arrays::stream)
                .parallel()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .limit(MAX_LINES)
                .collect(Collectors.toList());

            long t2 = System.currentTimeMillis();
            Logging.info("Read " + lines.size() + " lines in " + (t2-t1) + " ms");
            return new ProseLineSupplier(m_dictionary, lines);
        }
        catch (IOException e)
        {
            // Not much point in continuing
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    public PoetryLineSupplier readFileAsPoetry(@Nonnull String filename)
    {
        CSVFormat csvFormat = CSVFormat.DEFAULT
            .withSkipHeaderRecord()
            .withQuoteMode(QuoteMode.MINIMAL);

        try (
            BufferedReader reader = FileUtils.getBufferedReader(filename);
            //CSVParser parser = new CSVParser(reader, csvFormat)
        )
        {
            long t1 = System.currentTimeMillis();
            AtomicInteger numErrors = new AtomicInteger(0);

            List<Line> lines = reader.lines()
                .parallel()
                .map(line ->
                {
                    int firstQuoteIndex = line.indexOf('"');
                    int lastQuoteIndex = line.lastIndexOf('"');
                    if (firstQuoteIndex == -1 || lastQuoteIndex == -1)
                    {
                        return null;
                    }
                    return line.substring(firstQuoteIndex, lastQuoteIndex + 1);
                })
                .filter(Objects::nonNull)
                .map(lyrics -> lyrics.split("\\s*;;\\s*"))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .filter(s -> s.matches(".*[a-zA-Z].*"))
                .map(s ->
                {
                    try
                    {
                        return new Line(s, m_dictionary);
                    }
                    catch (Exception e)
                    {
                        Logging.debug(e.getMessage(), e);
                        numErrors.incrementAndGet();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            long t2 = System.currentTimeMillis();
            Logging.info("UrbanDictionaryReader - Read " + lines.size() + " lines in " + (t2-t1) + " ms with " + numErrors.get() + " errors");
            return new PoetryLineSupplier(m_dictionary, lines);
        }
        catch (IOException e)
        {
            // Not much point in continuing
            throw new RuntimeException(e);
        }
    }
}
