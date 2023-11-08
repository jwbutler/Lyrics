package lyrics;

import java.util.Set;
import javax.annotation.Nonnull;

import lyrics.dictionaries.Dictionary;

public interface RhymeMap
{
    @Nonnull
    Set<String> getRhymes(@Nonnull String key);
    
    @Nonnull
    static RhymeMap create(@Nonnull Dictionary dictionary)
    {
        return new LazyLoadedRhymeMap(dictionary);
        //return PrecomputedRhymeMap.create(dictionary);
    }
}
