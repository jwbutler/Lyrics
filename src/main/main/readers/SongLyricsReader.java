package main.readers;

import main.texts.PoetryLineSupplier;
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
 * @since July 2018
 */
public class SongLyricsReader
{
    @Nonnull
    private final IDictionary m_dictionary;

    public SongLyricsReader(@Nonnull IDictionary dictionary)
    {
        m_dictionary = dictionary;
    }

    @Nonnull
    public PoetryLineSupplier readFile(@Nonnull String filename)
    {
        try
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
                        return new Line(line, m_dictionary);
                    }
                    catch (Exception e)
                    {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

            return new PoetryLineSupplier(m_dictionary, lines);
        }
        catch (IOException e)
        {
            // Not much point in continuing
            throw new RuntimeException(e);
        }
    }
}
