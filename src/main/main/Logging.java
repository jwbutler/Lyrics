package main;

/**
 * @author jbutler
 * @since July 2018
 */
public class Logging {
  private static boolean DEBUG_ENABLED = true;
  public static void debug(String s) {
    if (DEBUG_ENABLED) {
      System.out.println(s);
    }
  }

  public static void debugF(String pattern, Object... args) {

    if (DEBUG_ENABLED) {
      System.out.printf(String.format(pattern + "\n", args));
    }
  }

  public static void debug(String s, Throwable e) {
    if (DEBUG_ENABLED) {
      System.out.println(s);
      e.printStackTrace();
    }
  }

  public static void error(String s) {
    System.out.println(s);
  }

  public static void error(String s, Throwable e) {
    System.out.println(s);
    e.printStackTrace();
  }
}
