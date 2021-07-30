package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// The Notebook class stores the player's note for each round,
//                    and give the player a chance to specify a number for each game.

public class Notebook implements Writable {
    public ArrayList<Note> notebook;
    public Integer specifiedNum = 0;
    public boolean hasSet;

    // EFFECTS: create a new ArrayList for recording notes
    public Notebook() {
        notebook = new ArrayList<>();
        hasSet = false;
    }

    // MODIFIES: this
    // EFFECTS: add a note to the notebook, plus the round number
    //          if the note is a single number and no number was specified
    //          record it as specifiedNum
    public void addNote(Note note) {
        Note nt = new Note(notebook.size() + 1 + ". " + note.content);
        notebook.add(nt);
        if (whetherSpecifyingNum(note)) {
            specifiedNum = Integer.valueOf(note.content);
        }
    }

    // MODIFIES: this
    // EFFECTS: add a note to the notebook
    public void addSimpleNote(Note note) {
        notebook.add(note);
    }

    // EFFECTS: determine whether the player is specifying a number
    public Boolean whetherSpecifyingNum(Note note) {
        if (note.content.length() == 1 && note.content.matches("\\d+")) {
            return specifiedNum == 0 && !note.content.equals("0");
        } else {
            return false;
        }
    }

    // EFFECTS: returns number of notes in this Notebook
    public int numNotes() {
        return notebook.size();
    }

    // EFFECTS: get the note at round n
    public Note getNote(int n) {
        return notebook.get(n - 1);
    }

    // MODIFIES: this, DisplayedEntries
    // EFFECTS: set the specified number in this class and the DisplayedEntries class to the given number
    public void setSpecifiedNum(int n, DisplayedEntries entries) {
        if (this.specifiedNum == 0 && !hasSet) {
            specifiedNum = n;
            hasSet = true;
            entries.setNum(n,this);
        }
    }

    // EFFECTS: returns things in this notebook as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Notebook", notesToJson());
        return json;
    }

    // EFFECTS: returns things in this notebook as a JSON array
    private JSONArray notesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Note n : notebook) {
            jsonArray.put(n.toJson());
        }

        return jsonArray;
    }
}
