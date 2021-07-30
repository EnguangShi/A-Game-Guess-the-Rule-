package model;

import exception.NotTheFirstRoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// The DisplayedEntries class represents the entries displayed on the screen.

public class DisplayedEntries implements Writable {
    public static final String UNKNOWN1 = "1. --- ----- ---- - ----- -- --- ------ -- -------, ---- ----- ------- .";
    public static final String UNKNOWN2 = "2. --- ----- ------ ------- -- ----- ----- -- --- ------ --- ------.";
    public static final String UNKNOWN3 = "3. ---- -- ----- -- --------, --- ----- ----- -- --------- -- -.";
    public static final String UNKNOWN4 = "4. --- --- ------ ------- -- ----- ----- -- --- ------ --- ------.";
    public static final String UNKNOWN5 = "5. ---- -- ----- -- -------, --- ----- ----- -- --------- -- -.";
    public static final String UNKNOWN6 = "6. ---- --- ----- ----- ------- -, --- ---- -------.";
    public static final String UNKNOWN7 = "7. ---- --- ------- --------- --- --------, --- ---.";
    public static final String UNKNOWN8 = "8. --- --- ------ --- ---- ------ -- -------- -- --- ---- ---- --- ----.";
    public static final String UNKNOWN9 = "9. -- - ----------- ----- -- ---------- ----, --- ---- -------.";
    public static final String EXPANDED1 = "1. The first time a green or red button is pressed, this entry expands.";
    public static final String EXPANDED2 = "2. The green button expands an entry based on the number you choose.";
    public static final String EXPANDED3 = "3. When an entry is expanded, the alpha count is increased by 1.";
    public static final String EXPANDED4 = "4. The red button removes an entry based on the number you choose.";
    public static final String EXPANDED5 = "5. When an entry is removed, the alpha count is increased by 1.";
    public static final String EXPANDED6 = "6. When the alpha count reaches 5, the game crashes.";
    public static final String EXPANDED7 = "7. When all entries displayed are expanded, you win.";
    public static final String EXPANDED8 = "8. You can choose the next number by entering in the note once per game.";
    public static final String EXPANDED9 = "9. If a nonexistent entry is interfered with, the game crashes.";

    public Map<Integer, String> entriesMap = new HashMap<>();
    public AllEntries allEntries = new AllEntries();
    public Game game = new Game();
    public int alphaCount;
    public int num = 0;
    public boolean hasSpecified;
    public boolean hasSet;

    // EFFECTS: create a map including all the unexpanded entries and set the fields to their initial states.
    public DisplayedEntries() {
        entriesMap.put(1, UNKNOWN1);
        entriesMap.put(2, UNKNOWN2);
        entriesMap.put(3, UNKNOWN3);
        entriesMap.put(4, UNKNOWN4);
        entriesMap.put(5, UNKNOWN5);
        entriesMap.put(6, UNKNOWN6);
        entriesMap.put(7, UNKNOWN7);
        entriesMap.put(8, UNKNOWN8);
        entriesMap.put(9, UNKNOWN9);
        alphaCount = 0;
        hasSpecified = false;
        hasSet = false;
    }

    // MODIFIES: this, Game
    // EFFECTS: if whetherIsFirstRound() is false, throw NotTheFirstRoundException
    //          otherwise expand Entry 1, add alpha by 1 and determine whether all entries are expanded
    public void expandEntry1ForTheFirstTime() throws NotTheFirstRoundException {
        if (!whetherIsFirstRound()) {
            throw new NotTheFirstRoundException();
        } else {
            entriesMap.remove(1);
            entriesMap.put(1, EXPANDED1);
            alphaPlusOneWhenExpanded();
            ifAllEntriesExpanded();
        }
    }

    // MODIFIES: this, Game
    // EFFECTS: if rule 2 exists, show the content of an entry based on the entry number
    //          if rule 2 doesn't exist or the entry has been expanded, ignore this method
    //          if the entry to be expanded doesn't exist, the game crashes.
    //          add alpha by 1 and determine whether all entries are expanded
    public void expandAnEntry(int number) {
        if (hasEntry(2)) {
            if (!hasExpanded(number)) {
                ifNotHaveEntry(number);
                try {
                    expandEntry1ForTheFirstTime();
                } catch (NotTheFirstRoundException e) {
                    System.out.println("Not the first round.");
                } finally {
                    if (hasEntry(number)) {
                        entriesMap.remove(number);
                        entriesMap.put(number, allEntries.entriesMap.get(number));
                        alphaPlusOneWhenExpanded();
                        ifAllEntriesExpanded();
                    }
                }
            }
        }
        System.out.println("alpha count = " + alphaCount);
    }

    // EFFECTS: determine whether an entry given the number has been expanded
    public boolean hasExpanded(int number) {
        return entriesMap.containsValue(allEntries.entriesMap.get(number));
    }

    // MODIFIES: this, Game
    // EFFECTS: if rule 3 exists, add alpha by 1
    //          if alpha reaches 5 afterwards, the game crashes
    public void alphaPlusOneWhenExpanded() {
        if (hasEntry(3)) {
            alphaCount += 1;
            ifAlphaReached5();
        }
    }

    // MODIFIES: this, Game
    // EFFECTS: if rule 4 exists, delete an entry based on the entry number
    //          if rule 4 doesn't exist, ignore this method
    //          if the entry to be deleted doesn't exist, the game crashes.
    //          add alpha by 1 and determine whether all entries are expanded
    public void removeAnEntry(int number) {
        if (hasEntry(4)) {
            ifNotHaveEntry(number);
            try {
                expandEntry1ForTheFirstTime();
            } catch (NotTheFirstRoundException e) {
                System.out.println("Not the first round.");
            } finally {
                entriesMap.remove(number);
                alphaPlusOneWhenRemoved();
                ifAllEntriesRemoved();
                ifAllEntriesExpanded();
            }
        }
        System.out.println("alpha count = " + alphaCount);
    }

    // MODIFIES: this, Game
    // EFFECTS: if rule 5 exists, add alpha by 1
    //          if alpha reaches 5 afterwards, the game crashes
    public void alphaPlusOneWhenRemoved() {
        if (hasEntry(5)) {
            alphaCount += 1;
            ifAlphaReached5();
        }
    }

    // MODIFIES: Game
    // EFFECTS: if rule 6 exists, the game crashes when alpha reaches 5
    public void ifAlphaReached5() {
        if (hasEntry(6)) {
            if (alphaCount >= 5) {
                game.crashBecauseAlpha();
            }
        }
    }

    // EFFECTS: determine have all entries been expanded
    public boolean hasAllEntriesExpanded() {
        Set keys = entriesMap.keySet();
        for (Object next : keys) {
            if (!allEntries.entriesMap.containsValue(entriesMap.get(next))) {
                return false;
            }
        }
        return true;
    }

    // MODIFIES: Game
    // EFFECTS: if rule 7 exists and all entries have been expanded, the game win
    public void ifAllEntriesExpanded() {
        if (hasEntry(7)) {
            if (hasAllEntriesExpanded()) {
                game.win();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if rule 8 exists, assigned the number given to the specified number
    public void specifyANumber(int number) {
        if (hasEntry(8)) {
            num = number;
            hasSpecified = true;
        }
    }

    // MODIFIES: Game
    // EFFECTS: if rule 9 exists, if the entry based on the given number doesn't exist, the game crashes
    public void ifNotHaveEntry(int number) {
        if (!hasEntry(number)) {
            if (hasEntry(9)) {
                game.crashBecauseInterference();
            }
        }
    }

    // EFFECTS: determine whether the entry list has an entry based on the given entry number
    public boolean hasEntry(int number) {
        return entriesMap.containsKey(number);
    }

    // MODIFIES: Game
    // EFFECTS: if all entries are removed, the game crashed because it runs out of move
    public void ifAllEntriesRemoved() {
        if (entriesMap.isEmpty()) {
            game.crashBecauseRunOutOfMoves();
        }
    }

    // EFFECTS: determine whether it is the first round of the game
    public boolean whetherIsFirstRound() {
        Set keys = entriesMap.keySet();
        int n = 1;
        for (Object next : keys) {
            if (!(next.equals(n) && !allEntries.entriesMap.containsValue(entriesMap.get(next)))) {
                return false;
            }
            n++;
        }
        return true;
    }

    // EFFECTS: returning whether the game has won
    public boolean hasWon() {
        return game.getHasWon();
    }

    // EFFECTS: returning whether the game has crashed because alpha reaches 5
    public boolean alphaCrash() {
        return game.getAlphaCrash();
    }

    // EFFECTS: returning whether the game has crashed because it runs out of moves
    public boolean movesCrash() {
        return game.getMovesCrash();
    }

    // EFFECTS: returning whether the game has crashed because non-existence entry was interfered with
    public boolean interfereCrash() {
        return game.getInterfereCrash();
    }

    // MODIFIES: this, Notebook
    // EFFECTS: set the specified number in this class and the Notebook class to the given number
    public void setNum(int n, Notebook notes) {
        if (this.num == 0 && !hasSet) {
            num = n;
            hasSet = true;
            notes.setSpecifiedNum(n, this);
        }
        this.hasSpecified = true;
    }

    // EFFECTS: returning things in DisplayedEntries as a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("DisplayedEntries", entriesToJson());
        return json;
    }

    // EFFECTS: returning things in DisplayedEntries as a json array
    private JSONArray entriesToJson() {
        JSONArray jsonArray = new JSONArray();
        Set keys = entriesMap.keySet();
        for (Object next : keys) {
            jsonArray.put(entryToJson(next));
        }
        if (hasSpecified) {
            jsonArray.put(numToJson());
        }
        return jsonArray;
    }

    // EFFECTS: returning things in json array as a json object
    private JSONObject entryToJson(Object next) {
        JSONObject entryJson = new JSONObject();
        entryJson.put(String.valueOf(next), entriesMap.get(next));
        return entryJson;
    }

    // EFFECTS: returning specified number in DisplayedEntries as a json object
    private JSONObject numToJson() {
        JSONObject numJson = new JSONObject();
        numJson.put("num", this.num);
        return numJson;
    }
}
