package main.utils;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author jbutler
 * @since July 2018
 */
public class FileUtils {
  @Nonnull
  public static Path getPath(@Nonnull String filename) {
    return Paths.get("resources", filename);
  }
}
