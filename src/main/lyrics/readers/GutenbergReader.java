package lyrics.readers;

import com.google.common.collect.ImmutableList;
import lyrics.Logging;
import lyrics.texts.LineSupplier;
import lyrics.texts.ProseLineSupplier;
import lyrics.utils.FileUtils;
import lyrics.dictionaries.Dictionary;
import lyrics.poetry.Line;
import lyrics.texts.PoetryLineSupplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
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
    private final Dictionary m_dictionary;

    public GutenbergReader(@Nonnull Dictionary dictionary)
    {
        m_dictionary = dictionary;
    }

    /**
     * @param lastLineBeforeStart Ignore all lines before this, if specified.
     */
    @Nonnull
    public LineSupplier readPoetryFile(
        @Nonnull String filename,
        @CheckForNull String lastLineBeforeStart,
        @CheckForNull String firstLineAfterEnd
    )
    {
        List<String> allLines = FileUtils.getBufferedReader(filename)
            .lines()
            .parallel()
            .map(String::trim)
            .collect(Collectors.toList());

        List<String> lines = _filterLines(allLines, lastLineBeforeStart, firstLineAfterEnd);

        List<Line> mappedLines = lines.stream()
            .parallel()
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
    public LineSupplier readPoetryFile(@Nonnull String filename)
    {
        return readPoetryFile(filename, null, null);
    }

    @Nonnull
    public LineSupplier readProseFile(@Nonnull String filename, @CheckForNull String lastLineBeforeStart, @CheckForNull String firstLineAfterEnd)
    {
        List<String> allLines = FileUtils.getBufferedReader(filename)
            .lines()
            .parallel()
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
}
