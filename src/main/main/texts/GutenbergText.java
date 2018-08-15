package main.texts;

import main.readers.GutenbergReader;

import javax.annotation.Nonnull;

/**
 * @author jbutler
 */
public enum GutenbergText
{
    PRIDE_AND_PREJUDICE(
        "prideandprejudice.txt",
        "Produced by Anonymous Volunteers",
        "End of the Project Gutenberg EBook of Pride and Prejudice, by Jane Austen"
    ),
    KANT_CRITIQUE_OF_PURE_REASON(
        "critiqueofpurereason.txt",
        "Produced by Charles Aldarondo and David Widger",
        "End of the Project Gutenberg EBook of The Critique of Pure Reason, by Immanuel Kant"
    ),
    ARISTOTLE_POETICS(
        "aristotle_poetics.txt",
        "Produced by Eric Eldred",
        "End of the Project Gutenberg EBook of The Poetics, by Aristotle"
    );

    @Nonnull
    private final String m_filename;
    @Nonnull
    private final String m_lastLineBeforeStart;
    @Nonnull
    private final String m_firstLineAfterEnd;

    GutenbergText(@Nonnull String filename, @Nonnull String lastLineBeforeStart, @Nonnull String firstLineAfterEnd)
    {
        m_filename = filename;
        m_lastLineBeforeStart = lastLineBeforeStart;
        m_firstLineAfterEnd = firstLineAfterEnd;
    }

    @Nonnull
    public ILineSupplier getLineSupplier(@Nonnull GutenbergReader reader)
    {
        return reader.readProseFile(m_filename, m_lastLineBeforeStart, m_firstLineAfterEnd);
    }
}
