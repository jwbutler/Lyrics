package main.readers;

import main.Text;
import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.utils.FileUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Reader interface intended to parse this file:
 * https://www.kaggle.com/mousehead/songlyrics#songdata.csv
 *
 * @author jbutler
 * @since
 */
public class SongLyricsReader
{
    private static final Random RNG = new Random();

    @Nonnull
    private final IDictionary m_dictionary;

    public SongLyricsReader(@Nonnull IDictionary dictionary) {
        m_dictionary = dictionary;
    }

    @Nonnull
    public Text readFile(@Nonnull String filename) throws IOException
    {
        return readFile(filename, 1.0);
    }
    @Nonnull
    public Text readFile(@Nonnull String filename, double keepPercentage) throws IOException
    {
        BufferedReader reader = Files.newBufferedReader(FileUtils.getPath(filename));
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withSkipHeaderRecord());
        List<CSVRecord> records = parser.getRecords();

        List<Line> lines = records.parallelStream()
            .map(r -> r.get(3))
            .map(lyrics -> lyrics.split("\\s*\n\\s*"))
            .flatMap(Arrays::stream)
            .parallel()
            .map(line ->
            {
                try
                {
                    Line parsedLine = new Line(line, m_dictionary);
                    return parsedLine;
                }
                catch (Exception e)
                {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .filter(unused -> RNG.nextDouble() < keepPercentage)
            .collect(Collectors.toList());

        List<String> words = lines.stream()
            .map(Line::getWords)
            .flatMap(List::stream)
            .collect(Collectors.toList());

        return new Text(lines, words);
    }
}
