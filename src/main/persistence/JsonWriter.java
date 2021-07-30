package persistence;

import model.DisplayedEntries;
import model.Notebook;
import org.json.JSONObject;


import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

// REFERENCE: JsonSerializationDemo
// Represents a writer that writes JSON representation of notebook to file

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        URL url = this.getClass().getResource(destination);
        try {
            File file = Paths.get(url.toURI()).toFile();
            writer = new PrintWriter(file);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of notebook to file
    public void writeNotebook(Notebook wr) {
        JSONObject json = wr.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of displayed entries to file
    public void writeEntries(DisplayedEntries en) {
        JSONObject json = en.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
