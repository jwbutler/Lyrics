package lyrics.utils;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * @author jbutler
 * @since July 2018
 */
public class FileUtils
{
    private FileUtils()
    {
    }

    @Nonnull
    public static BufferedReader getBufferedReader(@Nonnull String filename)
    {
        return Optional.of("/" + filename)
            .map(FileUtils.class::getResourceAsStream)
            .map(InputStreamReader::new)
            .map(BufferedReader::new)
            .orElseThrow(() -> new RuntimeException("Failed to read file " + filename));
    }
}
