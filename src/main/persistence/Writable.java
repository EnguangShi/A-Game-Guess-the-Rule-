package persistence;

import org.json.JSONObject;

// REFERENCE: JsonSerializationDemo
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
