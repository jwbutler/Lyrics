package main.readers;

import com.google.common.collect.ImmutableList;
import main.utils.FileUtils;
import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.utils.StringUtils;
import main.Text;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author jbutler
 * @since July 2018
 */
public class GutenbergReader
{
    @Nonnull
    private final IDictionary m_dictionary;

    public GutenbergReader(@Nonnull IDictionary dictionary)
    {
        m_dictionary = dictionary;
    }

    /**
     * @param lastInvalidLine Ignore all lines before this, if specified.
     */
    @Nonnull
    public Text readFile(@Nonnull String filename, @CheckForNull String lastInvalidLine) throws IOException
    {
        List<String> allLines = Files.lines(FileUtils.getPath(filename)).map(String::trim).collect(Collectors.toList());

        List<String> lines;

        if (lastInvalidLine != null)
        {
            int index = IntStream.range(0, allLines.size()).filter(i -> allLines.get(i).equals(lastInvalidLine)).findFirst().orElse(0);

            lines = ImmutableList.copyOf(allLines.subList(index + 1, allLines.size()));
        }
        else
        {
            lines = allLines;
        }

        String fullText = lines.stream().collect(Collectors.joining(" "));

        List<String> wordList = Stream.of(fullText.split("\\s+")).map(StringUtils::sanitize).map(String::toUpperCase).collect(Collectors.toList());

        List<Line> mappedLines = lines.stream().map(s ->
        {
            try
            {
                return new Line(s, m_dictionary);
            } catch (Exception e)
            {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return new Text(mappedLines, wordList);
    }

    @Nonnull
    public Text readFile(@Nonnull String filename) throws IOException
    {
        return readFile(filename, null);
    }
}
