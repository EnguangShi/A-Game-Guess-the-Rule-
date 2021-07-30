package persistence.test;

import model.DisplayedEntries;
import model.Notebook;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// REFERENCE: JsonSerializationDemo
public class JsonReaderTest {

    private static final String UNKNOWN1 = "1. --- ----- ---- - ----- -- --- ------ -- -------, ---- ----- ------- .";
    private static final String UNKNOWN2 = "2. --- ----- ------ ------- -- ----- ----- -- --- ------ --- ------.";
    private static final String UNKNOWN3 = "3. ---- -- ----- -- --------, --- ----- ----- -- --------- -- -.";
    private static final String UNKNOWN4 = "4. --- --- ------ ------- -- ----- ----- -- --- ------ --- ------.";
    private static final String UNKNOWN5 = "5. ---- -- ----- -- -------, --- ----- ----- -- --------- -- -.";
    private static final String UNKNOWN6 = "6. ---- --- ----- ----- ------- -, --- ---- -------.";
    private static final String UNKNOWN7 = "7. ---- --- ------- --------- --- --------, --- ---.";
    private static final String UNKNOWN8 = "8. --- --- ------ --- ---- ------ -- -------- -- --- ---- ---- --- ----.";
    private static final String UNKNOWN9 = "9. -- - ----------- ----- -- ---------- ----, --- ---- -------.";
    private static final String EXPANDED1 = "1. The first time a green or red button is pressed, this entry expands.";
    private static final String EXPANDED2 = "2. The green button expands an entry based on the number you choose.";
    private static final String EXPANDED3 = "3. When an entry is expanded, the alpha count is increased by 1.";
    private static final String EXPANDED4 = "4. The red button removes an entry based on the number you choose.";
    private static final String EXPANDED5 = "5. When an entry is removed, the alpha count is increased by 1.";
    private static final String EXPANDED6 = "6. When the alpha count reaches 5, the game crashes.";
    private static final String EXPANDED7 = "7. When all entries displayed are expanded, you win.";
    private static final String EXPANDED8 = "8. You can choose the next number by entering in the note once per game.";
    private static final String EXPANDED9 = "9. If a nonexistent entry is interfered with, the game crashes.";

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Notebook nb = reader.readNotebook();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyNotebook() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyNotebook.json");
        try {
            Notebook nb = reader.readNotebook();
            assertEquals(0, nb.numNotes());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderInitialEntries() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyEntries.json");
        try {
            DisplayedEntries en = reader.readEntries();
            assertEquals(9, en.entriesMap.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralNotebook() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralNotebook.json");
        try {
            Notebook nb = reader.readNotebook();
            assertEquals(2, nb.notebook.size());
            assertEquals("1. the first note", nb.getNote(1).content);
            assertEquals("2. the second note", nb.getNote(2).content);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralEntries() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralEntries.json");
        try {
            DisplayedEntries en = reader.readEntries();
            assertEquals(8, en.entriesMap.size());
            assertEquals(EXPANDED1, en.entriesMap.get(1));
            assertEquals(UNKNOWN2, en.entriesMap.get(2));
            assertEquals(null, en.entriesMap.get(3));
            assertEquals(UNKNOWN4, en.entriesMap.get(4));
            assertEquals(UNKNOWN5, en.entriesMap.get(5));
            assertEquals(UNKNOWN6, en.entriesMap.get(6));
            assertEquals(UNKNOWN7, en.entriesMap.get(7));
            assertEquals(UNKNOWN8, en.entriesMap.get(8));
            assertEquals(UNKNOWN9, en.entriesMap.get(9));
            int num = reader.readNumber();
            assertEquals(3, num);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
