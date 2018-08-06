package main.texts;

import main.poetry.Line;
import main.utils.MeterUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author jbutler
 * @since August 2018
 */
public class LineCache
{
    /**
     * A map of (meter -> (last word -> lines))
     * where last word is in uppercase
     */
    @Nonnull
    private final Map<List<Integer>, Map<String, List<Line>>> m_linesByMeter;
    public LineCache()
    {
        m_linesByMeter = new ConcurrentHashMap<>();
    }

    /**
     * @param lines All the lines in the corpus
     * @return a map of (last word -> line)
     *         where last word is in uppercase
     */
    @Nonnull
    public Map<String, List<Line>> getLinesByMeter(@Nonnull List<Line> lines, @Nonnull List<Integer> meter)
    {
        return m_linesByMeter.computeIfAbsent(meter, m ->
        {
            return lines.parallelStream()
                .filter(line -> MeterUtils.fitsMeter(m, line.getMeter()))
                .collect(Collectors.groupingBy(
                    line -> line.getWords().get(line.getWords().size() - 1).toUpperCase()
                ));
        });
    }
}
