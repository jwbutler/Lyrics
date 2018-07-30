package main.utils;

import javax.annotation.Nonnull;

/**
 * @author jbutler
 * @since July 2018
 */
public class StringUtils {
  @Nonnull
  public static String sanitize(@Nonnull String input) {
    return input.replaceAll("[^a-zA-Z0-9]", "");
  }
}
