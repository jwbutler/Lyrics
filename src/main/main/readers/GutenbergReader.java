package main.readers;

import com.google.common.collect.ImmutableList;
import main.Logging;
import main.texts.ProseLineSupplier;
import main.utils.FileUtils;
import main.dictionaries.IDictionary;
import main.poetry.Line;
import main.texts.PoetryLineSupplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * @param lastLineBeforeStart Ignore all lines before this, if specified.
     */
    @Nonnull
    public PoetryLineSupplier readPoetryFile(
        @Nonnull String filename,
        @CheckForNull String lastLineBeforeStart,
        @CheckForNull String firstLineAfterEnd
    )
    {
        try
        {
            List<String> allLines = Files.lines(FileUtils.getPath(filename))
                .map(String::trim)
                .collect(Collectors.toList());

            List<String> lines = _filterLines(allLines, lastLineBeforeStart, firstLineAfterEnd);

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
        catch (IOException e)
        {
            // Not much point continuing execution
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private List<String> _filterLines(@Nonnull List<String> allLines, @CheckForNull String lastLineBeforeStart, @CheckForNull String firstLineAfterEnd)
    {
        if (lastLineBeforeStart == null && firstLineAfterEnd == null)
        {
            return allLines;
        }

        int firstLineIndex = 0;
        int lastLineIndex = allLines.size() - 1;
        if (lastLineBeforeStart != null)
        {
            for (int i = 0; i < allLines.size(); i++)
            {
                if (allLines.get(i).equals(lastLineBeforeStart))
                {
                    firstLineIndex = i;
                    break;
                }
            }
        }

        if (firstLineAfterEnd != null)
        {
            for (int i = allLines.size() - 1; i >= 0; i--)
            {
                if (allLines.get(i).equals(firstLineAfterEnd))
                {
                    lastLineIndex = i;
                    break;
                }
            }
        }

        return ImmutableList.copyOf(allLines.subList(firstLineIndex, lastLineIndex + 1));
    }

    @Nonnull
    public PoetryLineSupplier readPoetryFile(@Nonnull String filename)
    {
        return readPoetryFile(filename, null, null);
    }

    @Nonnull
    public ProseLineSupplier readProseFile(@Nonnull String filename, @CheckForNull String lastLineBeforeStart, @CheckForNull String firstLineAfterEnd)
    {
        try
        {
            List<String> allLines = Files.lines(FileUtils.getPath(filename))
                .map(String::trim)
                .collect(Collectors.toList());

            List<String> lines = _filterLines(allLines, lastLineBeforeStart, firstLineAfterEnd);

            List<String> sentences = Arrays.stream(lines.stream()
                .collect(Collectors.joining(" "))
                .split(SENTENCE_ENDING_PUNCTUATION))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

            return new ProseLineSupplier(m_dictionary, sentences);
        }
        catch (IOException e)
        {
            // Not much point continuing execution
            throw new RuntimeException(e);
        }
    }
}
