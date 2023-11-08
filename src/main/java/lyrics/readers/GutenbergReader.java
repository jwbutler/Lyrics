package lyrics.readers;

import lyrics.Logging;
import lyrics.NoPronunciationException;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since July 2018
 */
public final class GutenbergReader
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
            .map(String::trim)
            .toList();

        List<String> lines = _filterLines(allLines, lastLineBeforeStart, firstLineAfterEnd);

        Set<Line> mappedLines = lines.stream()
            .map(s ->
            {
                try
                {
                    return Line.fromString(s, m_dictionary);
                }
                catch (NoPronunciationException e)
                {
                    return null;
                }
                catch (RuntimeException e)
                {
                    Logging.info(e.getMessage(), e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        return new PoetryLineSupplier(m_dictionary, mappedLines);
    }

    @Nonnull
    private static List<String> _filterLines(@Nonnull List<String> allLines, @CheckForNull String lastLineBeforeStart, @CheckForNull String firstLineAfterEnd)
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

        return allLines.subList(firstLineIndex, lastLineIndex + 1);
    }

    @Nonnull
    public LineSupplier readProseFile(@Nonnull String filename, @CheckForNull String lastLineBeforeStart, @CheckForNull String firstLineAfterEnd)
    {
        List<String> allLines = FileUtils.getBufferedReader(filename)
            .lines()
            .map(String::trim)
            .toList();

        List<String> lines = _filterLines(allLines, lastLineBeforeStart, firstLineAfterEnd);

        List<String> sentences = Arrays.stream(String.join(" ", lines).split(SENTENCE_ENDING_PUNCTUATION))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();

        return new ProseLineSupplier(m_dictionary, sentences);
    }
}
