package model;

import java.util.HashMap;
import java.util.Map;

// The AllEntries Class represents all the entries to replace the unexpanded ones in the Displayed Entries.

public class AllEntries {
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

    // EFFECTS: create a map with 9 expanded entries
    public AllEntries() {
        entriesMap.put(1, EXPANDED1);
        entriesMap.put(2, EXPANDED2);
        entriesMap.put(3, EXPANDED3);
        entriesMap.put(4, EXPANDED4);
        entriesMap.put(5, EXPANDED5);
        entriesMap.put(6, EXPANDED6);
        entriesMap.put(7, EXPANDED7);
        entriesMap.put(8, EXPANDED8);
        entriesMap.put(9, EXPANDED9);
    }
}
