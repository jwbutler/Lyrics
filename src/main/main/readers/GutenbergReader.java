package main.readers;

import com.google.common.collect.ImmutableList;
import main.Logging;
import main.texts.ProseLineSupplier;
import main.utils.FileUtils;
import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.texts.PoetryLineSupplier;
import main.utils.StringUtils;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jbutler
 * @since July 2018
 */
public class GutenbergReader
{
    private static final String SENTENCE_ENDING_PUNCTUATION = "[\\.!\\?]";
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
    public PoetryLineSupplier readPoetryFile(@Nonnull String filename, @CheckForNull String lastInvalidLine) throws IOException
    {
        List<String> allLines = Files.lines(FileUtils.getPath(filename))
            .map(String::trim)
            .collect(Collectors.toList());

        List<String> lines;

        if (lastInvalidLine != null)
        {
            int index = IntStream.range(0, allLines.size())
                .filter(i -> allLines.get(i).equals(lastInvalidLine))
                .findFirst().orElse(0);

            lines = ImmutableList.copyOf(allLines.subList(index + 1, allLines.size()));
        }
        else
        {
            lines = allLines;
        }

        List<Line> mappedLines = lines.stream()
            .map(s ->
            {
                try
                {
                    return new Line(s, m_dictionary);
                }
                catch (Exception e)
                {
                    Logging.debug(e.getMessage(), e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return new PoetryLineSupplier(m_dictionary, mappedLines);
    }

    @Nonnull
    public PoetryLineSupplier readPoetryFile(@Nonnull String filename) throws IOException
    {
        return readPoetryFile(filename, null);
    }

    public ProseLineSupplier readProseFile(@Nonnull String filename, @CheckForNull String lastInvalidLine) throws IOException
    {
        List<String> allLines = Files.lines(FileUtils.getPath(filename)).map(String::trim).collect(Collectors.toList());

        List<String> lines;

        if (lastInvalidLine != null)
        {
            int index = IntStream.range(0, allLines.size())
                .filter(i -> allLines.get(i).equals(lastInvalidLine))
                .findFirst().orElse(0);

            lines = ImmutableList.copyOf(allLines.subList(index + 1, allLines.size()));
        }
        else
        {
            lines = allLines;
        }

        List<String> sentences = Arrays.stream(
            lines.stream()
                .collect(Collectors.joining(" "))
                .split(SENTENCE_ENDING_PUNCTUATION)
        )
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
        return new ProseLineSupplier(m_dictionary, sentences);
    }
}
