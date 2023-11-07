package lyrics.meter;

import com.google.common.collect.ImmutableList;
import lyrics.linguistics.Emphasis;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author jwbutler
 * @since November 2019
 */
class MeterImpl implements Meter
{
    @Nonnull
    private final List<Emphasis> m_emphasisList;

    MeterImpl(@Nonnull List<Emphasis> emphasisList)
    {
        m_emphasisList = ImmutableList.copyOf(emphasisList);
    }

    @Override
    public int getNumSyllables()
    {
        return m_emphasisList.size();
    }

    @Nonnull
    @Override
    public Emphasis getEmphasis(int syllableNumber)
    {
        return m_emphasisList.get(syllableNumber);
    }

    @Override
    public boolean fitsLineMeter(@Nonnull Meter lineMeter)
    {
        if (lineMeter.getNumSyllables() != this.getNumSyllables())
        {
            return false;
        }

        for (int i = 0; i < this.getNumSyllables(); i++)
        {
            // Don't allow a strong syllable in a weak position
            if (lineMeter.getEmphasis(i) == Emphasis.STRONG && this.getEmphasis(i) != Emphasis.STRONG)
            {
                return false;
            }
            // Additionally, don't allow a weak syllable in a strong position at the end of the line
            if (i == this.getNumSyllables() - 1)
            {
                if (lineMeter.getEmphasis(i) == Emphasis.WEAK && this.getEmphasis(i) == Emphasis.STRONG)
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty()
    {
        return getNumSyllables() == 0;
    }
}
