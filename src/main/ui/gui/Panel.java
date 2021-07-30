package ui.gui;

import javax.swing.*;
import java.awt.*;

// The Panel Class represents a scrolling panel on the GUI
public class Panel extends JPanel {
    public  JTextPane textPane;
    public  JScrollPane scrollPane;

    // EFFECTS: it produces a panel of given width and height
    public Panel(int width, int height) {
        setLayout(null);
        setSize(new Dimension(width, height));
        scrollPane = new JScrollPane();
        setArea(width, height);
        textPane = new JTextPane();
        textPane.setEditable(false);
        scrollPane.setViewportView(textPane);
        add(scrollPane);
    }

    protected void setArea(int width, int height) {
        scrollPane.setBounds(0,0, width, height);
    }
}
