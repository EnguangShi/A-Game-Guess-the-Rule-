package ui.gui;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameFrame extends JFrame {
    private static final String JSON_STORE1 = "/notebook.json";
    private static final String JSON_STORE2 = "/displayed_entries.json";
    private JsonWriter jsonWriter1;
    private JsonReader jsonReader1;
    private JsonWriter jsonWriter2;
    private JsonReader jsonReader2;
    private Notebook notes;
    private boolean hasChanged;
    private Console console;
    private NotesArea noteArea;
    private Button buttonA;
    private Button buttonB;
    private Button buttonC;
    private Dices dices;
    private int choose;
    private int displayed;
    private DisplayedEntries entries;
    private Map<Integer, String> entriesBeforeMap;
    private int alpha = 0;

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;

    private List<Button> buttons = new ArrayList<>();

    // EFFECTS: prepare for saving and loading
    public GameFrame() {
        super("Please Press a Button");
        notes = new Notebook();
        entries = new DisplayedEntries();
        entriesBeforeMap = new HashMap<>();
        jsonWriter1 = new JsonWriter(JSON_STORE1);
        jsonReader1 = new JsonReader(JSON_STORE1);
        jsonWriter2 = new JsonWriter(JSON_STORE2);
        jsonReader2 = new JsonReader(JSON_STORE2);
        displayed = 0;
        createWindow();
    }

    // EFFECTS: create the window
    private void createWindow() {
        setSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        closingWindow();
        setLocationRelativeTo(null);
        add(new EntriesPanel());
        setVisible(true);
        createButtons();
        setVisible(true);
        createNoteArea();
        setVisible(true);
        createConsole();
        setVisible(true);
        startGame();
    }

    // EFFECTS: start the game by asking whether new game or load game
    private void startGame() {
        hasChanged = false;
        try {
            if (jsonReader1.readNotebook().numNotes() == 0) {
                processCommand();
            } else {
                addTextToConsole("New Game or Load Game?\n\n");
                buttonA.button.setText("New Game");
                buttonC.button.setText("Load Game");
            }
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: ask the player to roll the dices
    private void processCommand() {
        try {
            addTextToConsole(
                    "****************************************************************************************\n\n");
            addTextToConsole("There are two 9-sided dices on the table.\n\n\n\n\n");
            buttonB.button.setText("Roll the dices");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: roll two dices and present the numbers got
    private void rollTwoDices() {
        dices = new Dices();
        playSound();
        try {
//            addText("rolling on the desk\n\n");
//            waitTwoSeconds();
            dices.roll();
            addTextToConsole("Left dice gets " + dices.dice1 + ".\n\n");
            addTextToConsole("Right dice gets " + dices.dice2 + ".\n\n");
            chooseADice();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: let the player choose 1 of the 2 dices or neither
    private void chooseADice() {
        try {
            addTextToConsole("Which dice do you want?\n\n\n\n\n");
            buttonA.button.setText(String.valueOf(dices.dice1));
            buttonC.button.setText(String.valueOf(dices.dice2));
//            waitTwoSeconds();
            buttonB.button.setText("Neither, roll a third dice");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: tell the player the number he/she chose
    private void chooseDiceOneOrTwo() {
        try {
            addTextToConsole("You chose " + choose + ".\n\n");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        chooseFromTwoButtons();
    }

    // EFFECTS: if the player chose neither,
    //          roll a third dice and tell the player the number he must use
    private void rollTheThirdDice() {
        Random random = new Random();
        playSound();
        try {
            addTextToConsole("Ok, throwing the hidden third dice.\n\n");
//            waitTwoSeconds();
            choose = random.nextInt(9) + 1;
            displayed = choose;
            addTextToConsole("It gets " + choose + ". You will use this number.\n\n");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        chooseFromTwoButtons();
    }

    // EFFECTS: let the user choose from the green or red button
    private void chooseFromTwoButtons() {
        try {
            addTextToConsole("Please press a button you want.\n\n\n\n\n");
            Set keys = entries.entriesMap.keySet();
            for (Object next: keys) {
                entriesBeforeMap.put((Integer) next, entries.entriesMap.get(next));
            }
            buttonA.button.setText("GREEN");
            buttonA.button.setBackground(Color.GREEN);
            buttonA.button.setOpaque(true);
            buttonC.button.setText("RED");
            buttonC.button.setBackground(Color.RED);
            buttonC.button.setOpaque(true);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: ask the player if he wants to take note (specify a number)
    private void takeSomeNotes() {
        try {
            if (sameEntries()) {
                addTextToConsole("Nothing has changed.\n\n");
            } else {
                addTextToConsole("Some changes have emerged above.\n\n");
            }
            entriesBeforeMap.clear();
            addTextToConsole("Do you want to take some note?\n\n\n\n\n");
            buttonA.button.setText("Yes");
            buttonC.button.setText("No");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: determine if two maps of entries are identical
    private boolean sameEntries() {
        Set keys = entriesBeforeMap.keySet();
        for (Object next: keys) {
            if (!entriesBeforeMap.get(next).equals(entries.entriesMap.get(next))) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: put the note entered by the player into the note area
    private void showInputWindow() {
        String str = JOptionPane.showInputDialog(this, "Enter note:",
                "Taking note", JOptionPane.PLAIN_MESSAGE);
        if (str != null) {
            Scanner scanner = new Scanner(str);
            try {
                String n = scanner.nextLine();
                specifyingNum(n);
            } catch (NoSuchElementException e) {
                showNoNote();
            }
        } else {
            showNoNote();
        }
    }

    // EFFECTS: determine whether the user is specifying a number
    //          if yes, record it in the note and change the specified number
    //          if no, record the note
    private void specifyingNum(String n) {
        try {
            Note note = new Note(n);
            if (notes.whetherSpecifyingNum(note)) {
                notes.addNote(note);
                addTextToNoteArea(notes.notebook.size() + ". " + note.content + "\n");
                entries.specifyANumber(notes.specifiedNum);
                addTextToConsole(
                        "****************************************************************************************\n\n");
                addTextToConsole("You chose " + entries.num + " for round " + (notes.notebook.size() + 1) + ".\n\n");
                choose = entries.num;
                displayed = choose;
                chooseFromTwoButtons();
            } else {
                notes.addNote(note);
                addTextToNoteArea(notes.notebook.size() + ". " + note.content + "\n");
                noteRecorded();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: tell the player that the note has been recorded
    private void noteRecorded() {
        try {
            addTextToConsole("Your note has been recorded.\n\n");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        processCommand();
    }

    // EFFECTS: produce a message in the note area indicating that no note was entered
    private void showNoNote() {
        try {
            Note note = new Note("*****no note*****");
            notes.addNote(note);
            addTextToNoteArea(notes.notebook.size() + ". " + note.content + "\n");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        processCommand();
    }

    // EFFECTS: ask the player if he/she wants to save the game when closing the window
    //          exit after whatever the action is
    private void closingWindow() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (hasChanged) {
                    int option = JOptionPane.showConfirmDialog(
                            GameFrame.this, "Do you want to save the game?", "Reminder",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        if (e.getWindow() == GameFrame.this) {
                            saveNotebook();
                            System.exit(0);
                        }
                    } else if (option == JOptionPane.NO_OPTION) {
                        if (e.getWindow() == GameFrame.this) {
                            System.exit(0);
                        }
                    }
                } else {
                    System.exit(0);
                }
            }
        });
    }

    // EFFECTS: save the notebook
    private void saveNotebook() {
        try {
            jsonWriter1.open();
            jsonWriter1.writeNotebook(notes);
            jsonWriter1.close();
            jsonWriter2.open();
            jsonWriter2.writeEntries(entries);
            jsonWriter2.close();
            System.out.println("Saved Notebook to" + JSON_STORE1);
            System.out.println("Saved Entries to" + JSON_STORE2);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE1 + " or " + JSON_STORE2);
        }
    }

    // EFFECTS: load the notebook
    private void loadNotebook() {
        try {
            notes = jsonReader1.readNotebook();
            entries = jsonReader2.readEntries();
            notes.setSpecifiedNum(jsonReader2.readNumber(), entries);
            System.out.println("Loaded Notebook from " + JSON_STORE1);
            System.out.println("Loaded Entries from " + JSON_STORE2);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE1 + " or " + JSON_STORE2);
        }

        repaint(0,0,1000,400);
        console.scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        if (!(notes.numNotes() == 0)) {
            for (Note next : notes.notebook) {
                try {
                    addTextToNoteArea(next.content + "\n");
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // EFFECTS: create three buttons
    private void createButtons() {
        JPanel buttonArea = new JPanel();
        buttonArea.setLayout(new GridLayout(1, 0));
        buttonArea.setSize(new Dimension(0, 0));
        add(buttonArea, BorderLayout.SOUTH);

        buttonA = new Button(buttonArea);
        buttons.add(buttonA);
        addListenerA();

        buttonB = new Button(buttonArea);
        buttons.add(buttonB);
        addListenerB();

        buttonC = new Button(buttonArea);
        buttons.add(buttonC);
        addListenerC();
    }

    // EFFECTS: create the console
    private void createConsole() {
        try {
            console = new Console(650, 350);
            getContentPane().add(console);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: create the note area
    private void createNoteArea() {
        try {
            noteArea = new NotesArea(350, 350);
            getContentPane().add(noteArea);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: adds a listener for buttonA
    public void addListenerA() {
        buttonA.button.addActionListener(new GameFrame.ButtonClickHandlerA());
    }

    public class ButtonClickHandlerA implements ActionListener {

        // EFFECTS: identify different method according to different names of the button
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonA.button.getText().equals("New Game")) {
                clearAllButtons();
                processCommand();
            } else if (buttonA.button.getText().equals(String.valueOf(dices.dice1))) {
                clearAllButtons();
                choose = dices.dice1;
                displayed = choose;
                chooseDiceOneOrTwo();
            } else if (buttonA.button.getText().equals("GREEN")) {
                clearAllButtons();
                entries.expandAnEntry(choose);
                repaint(0,0,1000,400);
                console.scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
                choose = 0;
                wonCrashedOrNeither();
            } else if (buttonA.button.getText().equals("Yes")) {
                clearAllButtons();
                showInputWindow();
            }
        }
    }

    // EFFECTS: adds a listener for buttonB
    public void addListenerB() {
        buttonB.button.addActionListener(new GameFrame.ButtonClickHandlerB());
    }

    public class ButtonClickHandlerB implements ActionListener {

        // EFFECTS: identify different method according to different names of the button
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonB.button.getText().equals("Roll the dices")) {
                clearAllButtons();
                hasChanged = true;
                rollTwoDices();
            } else if (buttonB.button.getText().equals("Neither, roll a third dice")) {
                clearAllButtons();
                rollTheThirdDice();
            }
        }
    }

    // EFFECTS: adds a listener for buttonC
    public void addListenerC() {
        buttonC.button.addActionListener(new GameFrame.ButtonClickHandlerC());
    }

    public class ButtonClickHandlerC implements ActionListener {

        // EFFECTS: identify different method according to different names of the button
        //          called by the framework when the button is clicked
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonC.button.getText().equals("Load Game")) {
                clearAllButtons();
                loadNotebook();
                processCommand();
            } else if (buttonC.button.getText().equals(String.valueOf(dices.dice2))) {
                clearAllButtons();
                choose = dices.dice2;
                displayed = choose;
                chooseDiceOneOrTwo();
            } else if (buttonC.button.getText().equals("RED")) {
                clearAllButtons();
                entries.removeAnEntry(choose);
                repaint(0,0,1000,400);
                console.scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
                choose = 0;
                wonCrashedOrNeither();
            } else if (buttonC.button.getText().equals("No")) {
                clearAllButtons();
                showNoNote();
            }
        }
    }

    // EFFECTS: if the player won or the game crashed, execute the wonOrCrashed to pop out window
    //          if no, let the user take notes
    private void wonCrashedOrNeither() {
        if (hasWonOrCrashed()) {
            wonOrCrashed();
        } else {
            takeSomeNotes();
        }
    }

    // EFFECTS: determine whether the player won or the game crashed
    private boolean hasWonOrCrashed() {
        return (entries.hasWon() || entries.alphaCrash() || entries.movesCrash() || entries.interfereCrash());
    }

    // EFFECTS: pop out windows for different game status
    private void wonOrCrashed() {
        if (entries.hasWon()) {
            ifWon();
        } else if (entries.alphaCrash()) {
            ifAlphaCrash();
        } else if (entries.movesCrash()) {
            ifMovesCrash();
        } else if (entries.interfereCrash()) {
            ifInterfereCrash();
        }
    }

    // EFFECTS: pop out the window showing the player wins
    private void ifWon() {
        int option = JOptionPane.showConfirmDialog(
                GameFrame.this, "You Win!", "Congratulations!",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    // EFFECTS: pop out the window showing the game crashes because of alpha reaching 5
    private void ifAlphaCrash() {
        int option = JOptionPane.showConfirmDialog(
                GameFrame.this,
                "!!!!!Alpha Overflow Error!!!!!\n\n" + "Hint: remove an entry to disable it",
                "The Game Crashed. You Lose.",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    // EFFECTS: pop out the window showing the game crashes because of no moves left
    private void ifMovesCrash() {
        int option = JOptionPane.showConfirmDialog(
                GameFrame.this,
                "!!!!!Running out of Possible Steps!!!!!\n\n" + "Hint: remove an entry to disable it",
                "The Game Crashed. You Lose.",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    // EFFECTS: pop out the window showing the game crashes because of interference with non-existence entry
    private void ifInterfereCrash() {
        int option = JOptionPane.showConfirmDialog(
                GameFrame.this,
                "!!!!!Interference with Void!!!!!\n\n" + "Hint: remove an entry to disable it",
                "The Game Crashed. You Lose.",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    // EFFECTS: clear the names of all buttons
    private void clearAllButtons() {
        buttonA.button.setText(null);
        buttonB.button.setText(null);
        buttonC.button.setText(null);
        buttonA.button.setBackground(null);
        buttonB.button.setBackground(null);
        buttonC.button.setBackground(null);
    }

//    private void waitTwoSeconds() {
//        for (int i = 0; i < 4; i++) {
//            try {
//                Thread.sleep(500);
//                addText(".\n");
//            } catch (BadLocationException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // EFFECTS: add text in the console
    private void addTextToConsole(String text) throws BadLocationException {
        console.textPane.getDocument().insertString(console.textPane.getDocument().getLength(), text, null);
        console.textPane.setCaretPosition(console.textPane.getDocument().getLength());
    }

    // EFFECTS: add text in the note area
    private void addTextToNoteArea(String text) throws BadLocationException {
        noteArea.textPane.getDocument().insertString(noteArea.textPane.getDocument().getLength(), text, null);
        noteArea.textPane.setCaretPosition(noteArea.textPane.getDocument().getLength());
    }

    // EFFECTS: play the dice sound effect
    private void playSound() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(this.getClass().getResource("/DicesSoundEffect.wav")));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    // The EntriesPanel Class represents the panel that shows the rule
    public class EntriesPanel extends JComponent {
        public static final int RULES_X = 150;
        public static final int RULES_Y = 50;

        // construct the rule panel
        public void paintComponent(Graphics g) {
            int y = RULES_Y;
            g.setFont(new Font(null, Font.PLAIN, 18));
            Set keys = entries.entriesMap.keySet();
            for (Object next : keys) {
                g.drawString(entries.entriesMap.get(next), RULES_X, y);
                y += 40;
            }
            if (displayed != 0) {
                g.drawString("You chose " + String.valueOf(displayed), 820,200);
            }
        }
    }
}

