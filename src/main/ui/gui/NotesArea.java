package ui.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

// The NotesWindow Class represents the note area on the GUI
public class NotesArea extends Panel {

    // EFFECTS: construct a scrolling note window panel
    public NotesArea(int width, int height) throws BadLocationException {
        super(width, height);
    }

    // MODIFIES: this
    // EFFECTS: set the size of the note window
    @Override
    public void setArea(int width, int height) {
        scrollPane.setBounds(655, 400, width - 10, height - 10);
    }
}
