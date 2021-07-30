package model.test;

import model.DisplayedEntries;
import model.Game;
import model.Notebook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private DisplayedEntries entries;
    private Game game = new Game();
    private Notebook notes = new Notebook();
    private static final String UNKNOWN1 = "1. ---- ------- ---- --- ----- ---- - ----- -- --- ------ -- -------.";
    private static final String UNKNOWN2 = "2. --- ----- ------ ------- -- ----- ----- -- --- ------ --- ------.";
    private static final String UNKNOWN3 = "3. ---- -- ----- -- --------, --- ----- ----- -- --------- -- -.";
    private static final String UNKNOWN4 = "4. --- --- ------ ------- -- ----- ----- -- --- ------ --- ------.";
    private static final String UNKNOWN5 = "5. ---- -- ----- -- -------, --- ----- ----- -- --------- -- -.";
    private static final String UNKNOWN6 = "6. ---- --- ----- ----- ------- -, --- ---- -------.";
    private static final String UNKNOWN7 = "7. ---- --- ------- --------- --- --------, --- ---.";
    private static final String UNKNOWN8 = "8. --- --- ------ --- ---- ------ -- -------- -- --- ---- ---- --- ----.";
    private static final String UNKNOWN9 = "9. -- - ----------- ----- -- ---------- ----, --- ---- -------.";
    private static final String EXPANDED1 = "1. This expands when the first time a green or red button is pressed.";
    private static final String EXPANDED2 = "2. The green button expands an entry based on the number you choose.";
    private static final String EXPANDED3 = "3. When an entry is expanded, the alpha count is increased by 1.";
    private static final String EXPANDED4 = "4. The red button removes an entry based on the number you choose.";
    private static final String EXPANDED5 = "5. When an entry is removed, the alpha count is increased by 1.";
    private static final String EXPANDED6 = "6. When the alpha count reaches 5, the game crashes.";
    private static final String EXPANDED7 = "7. When all entries displayed are expanded, you win.";
    private static final String EXPANDED8 = "8. You can choose the next number by entering in the note once per game.";
    private static final String EXPANDED9 = "9. If a nonexistent entry is interfered with, the game crashes.";

    @BeforeEach
    public void runBefore() {
        entries = new DisplayedEntries();
    }

    @Test
    public void testGame1() {
        entries.expandAnEntry(2);
        assertEquals(EXPANDED2, entries.entriesMap.get(2));
        assertEquals(EXPANDED1, entries.entriesMap.get(1));
        assertEquals(2, entries.alphaCount);
        assertTrue(entries.hasExpanded(2));
        entries.removeAnEntry(6);
        assertFalse(entries.entriesMap.containsValue(UNKNOWN6));
        assertEquals(3, entries.alphaCount);
        entries.setNum(1, notes);
        assertEquals(1, entries.num);
        assertTrue(entries.hasSet);
        assertTrue(entries.hasSpecified);
        entries.hasSpecified = false;
        entries.specifyANumber(2);
        assertEquals(2, entries.num);
        entries.setNum(6, notes);
        assertEquals(2, entries.num);
        assertTrue(entries.hasSpecified);
        assertFalse(entries.hasEntry(6));
        entries.expandAnEntry(3);
        entries.expandAnEntry(4);
        entries.expandAnEntry(5);
        entries.expandAnEntry(7);
        entries.expandAnEntry(8);
        entries.expandAnEntry(9);
        assertTrue(entries.hasAllEntriesExpanded());
        assertTrue(entries.hasWon());
    }

    @Test
    public void testGame2() {
        assertFalse(entries.movesCrash());
        entries.removeAnEntry(6);
        entries.removeAnEntry(1);
        entries.removeAnEntry(2);
        entries.expandAnEntry(3);
        entries.removeAnEntry(3);
        entries.alphaPlusOneWhenExpanded();
        assertEquals(5, entries.alphaCount);
        entries.removeAnEntry(5);
        entries.removeAnEntry(6);
        entries.removeAnEntry(7);
        entries.removeAnEntry(8);
        entries.specifyANumber(5);
        assertEquals(0, entries.num);
        entries.removeAnEntry(9);
        entries.ifNotHaveEntry(8);
        assertFalse(game.interfereCrash);
        entries.removeAnEntry(4);
        entries.removeAnEntry(4);
        assertTrue(entries.movesCrash());
    }

    @Test
    public void testGame3() {
        assertFalse(entries.alphaCrash());
        entries.hasSet = true;
        entries.setNum(1, notes);
        assertEquals(0, entries.num);
        entries.expandAnEntry(2);
        entries.expandAnEntry(2);
        entries.expandAnEntry(3);
        entries.expandAnEntry(4);
        entries.expandAnEntry(5);
        assertTrue(entries.alphaCrash());
    }

    @Test
    public void testGame4() {
        entries.removeAnEntry(1);
        entries.removeAnEntry(1);
        assertTrue(entries.interfereCrash());
    }


}
