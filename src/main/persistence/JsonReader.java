package persistence;

import model.DisplayedEntries;
import model.Note;
import model.Notebook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

// REFERENCE: JsonSerializationDemo
// Represents a reader that reads notebook from JSON data stored in file

public class JsonReader {
    private String source;
    private int number;
    private ArrayList removeList;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads notebook from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Notebook readNotebook() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseNotebook(jsonObject);
    }

    // EFFECTS: reads entries from file and returns it;
    // throws IOException if an error occurs reading data from file
    public DisplayedEntries readEntries() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseEntries(jsonObject);
    }

    // EFFECTS: reads entries from file and returns it;
    // throws IOException if an error occurs reading data from file
    public int readNumber() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseNumber(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (InputStream is = this.getClass().getResource(source).openStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                try (Stream<String> stream = reader.lines()) {
                    stream.forEach(contentBuilder::append);
                }
            }
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses notebook from JSON object and returns it
    private Notebook parseNotebook(JSONObject jsonObject) {
        Notebook nb = new Notebook();
        addNotes(nb, jsonObject);
        return nb;
    }

    // EFFECTS: parses entries from JSON object and returns it
    private DisplayedEntries parseEntries(JSONObject jsonObject) {
        DisplayedEntries en = new DisplayedEntries();
        addEntry(en, jsonObject);
        return en;
    }

    // EFFECTS: parses entries from JSON object and returns it
    private int parseNumber(JSONObject jsonObject) {
        this.number = 0;
        addNumber(jsonObject);
        return this.number;
    }

    // MODIFIES: nb
    // EFFECTS: parses notes from JSON object and adds them to notebook
    private void addNotes(Notebook nb, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Notebook");
        for (Object json : jsonArray) {
            JSONObject nextNote = (JSONObject) json;
            addNote(nb, nextNote);
        }
    }

    // MODIFIES: en
    // EFFECTS: parses entry from JSON object and adds them to notebook
    private void addEntry(DisplayedEntries en, JSONObject jsonObject) {
        removeList = new ArrayList();
        loopThroughFile(en, jsonObject);
        for (Object next : removeList) {
            en.entriesMap.remove(next);
        }
    }

    // MODIFIES: this
    // EFFECTS: if a rule exists in the document, update it
    //          if it doesn't exist in the document, remove it
    private void loopThroughFile(DisplayedEntries en, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("DisplayedEntries");
        Set keys = en.entriesMap.keySet();
        for (Object next : keys) {
            boolean found = false;
            tag:
            for (Object json : jsonArray) {
                jsonObject = (JSONObject) json;
                Set<String> jsonKeys = jsonObject.keySet();
                for (String nextJsonKey : jsonKeys) {
                    if (nextJsonKey.equals(String.valueOf(next))) {
                        if (!nextJsonKey.equals("num")) {
                            en.entriesMap.put(Integer.parseInt(nextJsonKey), jsonObject.getString(nextJsonKey));
                        }
                        found = true;
                        break tag;
                    }
                }
            }
            if (!found) {
                removeList.add((Integer) next);
            }
        }
    }

    // MODIFIES: en
    // EFFECTS: parses entry from JSON object and adds them to notebook
    private void addNumber(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("DisplayedEntries");
        for (Object json : jsonArray) {
            jsonObject = (JSONObject) json;
            Set<String> keys = jsonObject.keySet();
            for (String next : keys) {
                if (next.equals("num")) {
                    this.number = jsonObject.getInt(next);
                }
            }
        }
    }

    // MODIFIES: nb
    // EFFECTS: parses note from JSON object and adds it to notebook
    private void addNote(Notebook nb, JSONObject jsonObject) {
        String note = jsonObject.getString("note");
        Note nt = new Note(note);
        nb.addSimpleNote(nt);
    }
}
