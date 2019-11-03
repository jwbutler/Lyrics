package lyrics;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * @author jbutler
 * @since July 2018
 */
@Immutable
public class Pair<S, T> {
  private final S m_s;
  private final T m_t;

  private Pair(@CheckForNull S s, @CheckForNull T t) {
    m_s = s;
    m_t = t;
  }

  /**
   * This is preferable to the basic constructor because it does type inference for you
   */
  @Nonnull
  public static <A, B> Pair<A, B> of(@CheckForNull A s, @CheckForNull B t) {
    return new Pair<>(s, t);
  }

  @CheckForNull
  public S getFirst() {
    return m_s;
  }

  @CheckForNull
  public T getSecond() {
    return m_t;
  }
}
