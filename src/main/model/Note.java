package model;

import org.json.JSONObject;
import persistence.Writable;

// The Note class represents the note that the player enters each round

public class Note implements Writable {
    public String content;

    // EFFECTS: create a new note with a string type content
    public Note(String str) {
        content = str;
    }

    // EFFECTS: returning things in note as a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("note", content);
        return json;
    }
}
