package ui.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

// The Console Class represents the console on the GUI
public class Console extends Panel {

    // EFFECTS: construct a scrolling console panel
    public Console(int width, int height) throws BadLocationException {
        super(width, height);
    }

    // MODIFIES: this
    // EFFECTS: set the size of the console
    @Override
    public void setArea(int width, int height) {
        scrollPane.setBounds(5, 400, width - 10, height - 10);
    }

}
