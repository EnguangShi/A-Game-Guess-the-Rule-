package model.test;

import model.Note;
import model.Notebook;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class NotebookTest {

    private Notebook testNotebook = new Notebook();

    @Test
    public void testAddNote() {
        Note note1 = new Note("Entry 1 means something");
        testNotebook.addNote(note1);
        Note note2 = new Note("Entry 2 means something");
        testNotebook.addNote(note2);
        Note note3 = new Note("Entry 3 means something");
        testNotebook.addNote(note3);
        assertEquals("1. Entry 1 means something", testNotebook.getNote(1).content);
        assertEquals("2. Entry 2 means something", testNotebook.getNote(2).content);
        assertEquals("3. Entry 3 means something", testNotebook.getNote(3).content);
        assertEquals(0, testNotebook.specifiedNum);

        Note note4 = new Note("?");
        testNotebook.addNote(note4);
        assertEquals(0, testNotebook.specifiedNum);

        Note note5 = new Note("0");
        testNotebook.addNote(note5);
        assertEquals(0, testNotebook.specifiedNum);

        Note note6 = new Note("11");
        testNotebook.addNote(note6);
        assertEquals(0, testNotebook.specifiedNum);

        Note note7 = new Note("1ab");
        testNotebook.addNote(note7);
        assertEquals(0, testNotebook.specifiedNum);

        Note note8 = new Note("1");
        testNotebook.addNote(note8);
        assertEquals(1, testNotebook.specifiedNum);

        Note note9 = new Note("2");
        testNotebook.addNote(note9);
        assertEquals(1, testNotebook.specifiedNum);

        Note note10 = new Note("Entry 4 means something");
        testNotebook.addNote(note10);
        assertEquals("10. Entry 4 means something", testNotebook.getNote(10).content);
    }
}
