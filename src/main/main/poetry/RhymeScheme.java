package main.poetry;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jbutler
 * @since July 2018
 */
public class RhymeScheme
{
    @Nonnull
    private List<Character> m_rhymes;
    public RhymeScheme(@Nonnull String description)
    {
        Preconditions.checkArgument(description.matches("[A-Z]+"));
    }
}
