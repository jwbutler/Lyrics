package lyrics.utils;

import javax.annotation.Nonnull;

/**
 * @author jbutler
 * @since July 2018
 */
public class StringUtils {
  @Nonnull
  public static String alphanumericOnly(@Nonnull String input) {
    return input.replaceAll("[^a-zA-Z0-9]", "");
  }

  @Nonnull
  public static String alphabeticOnly(@Nonnull String input) {
    return input.replaceAll("[^a-zA-Z]", "");
  }
}
