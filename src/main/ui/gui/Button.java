package ui.gui;

import javax.swing.*;

// EFFECTS: The Button Class represents the buttons on the GUI
public class Button {
    public JButton button;

    // EFFECTS: construct buttons
    public Button(JComponent parent) {
        createButton(parent);
        addToParent(parent);

    }

    // MODIFIES: this
    // EFFECTS: customize the button
    public JButton customizeButton(JButton button) {
        button.setBorderPainted(true);
        button.setFocusPainted(true);
        button.setContentAreaFilled(true);
        return button;
    }

    // EFFECTS: creates button to activate tool
    public void createButton(JComponent parent) {
        button = new JButton();
        button = customizeButton(button);
        addToParent(parent);
    }

    // MODIFIES: parent
    // EFFECTS:  adds the given button to the parent component
    public void addToParent(JComponent parent) {
        parent.add(button);
    }




}
