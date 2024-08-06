package persistence;
import main.SR;
import main.HexStone;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads sr from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads sr from file and returns it;
    // throws IOException if an error occurs reading data from file
    public SR read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSR(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    // Method taken from JSONReader class in
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses sr from JSON object and returns it
    private SR parseSR(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        SR sr = new SR(name);
        addInfo(sr, jsonObject);
        return sr;
    }

    // MODIFIES: sr
    // EFFECTS: parses arrays from JSON object and adds them to SR
    private void addInfo(SR sr, JSONObject jsonObject) {

        sr.red_counter = jsonObject.getInt("red_counter");
        sr.blue_counter = jsonObject.getInt("blue_counter");
        sr.placed_stones = jsonObject.getInt("placed_stones");
        sr.curr = jsonObject.getInt("curr");

        JSONArray jsonArray0 = jsonObject.getJSONArray("WillBeFlipped");
        for (Object json : jsonArray0) {
            JSONObject next = (JSONObject) json;
            addHexStone(sr, next, "WBF");
        }

        JSONArray jsonArray1 = jsonObject.getJSONArray("stone_array");
        for (Object json : jsonArray1) {
            JSONObject next = (JSONObject) json;
            addHexStone(sr, next, "stone");
        }

        JSONArray jsonArray2 = jsonObject.getJSONArray("loadBoard");
        for (Object json : jsonArray2) {
            JSONObject next = (JSONObject) json;
            addHexStone(sr, next, "board");
        }

        JSONArray jsonArray3 = jsonObject.getJSONArray("loadIC");
        for (Object json : jsonArray3) {
            boolean b = (Boolean) json;
            sr.addBooleanArrays(b, 1);
        }

        JSONArray jsonArray4 = jsonObject.getJSONArray("loadIL");
        for (Object json : jsonArray4) {
            boolean b = (Boolean) json;
            sr.addBooleanArrays(b, 2);
        }


    }

    // MODIFIES: sr
    // EFFECTS: parses hexstone from JSON object and adds it to array in SR
    private void addHexStone(SR sr, JSONObject jsonObject, String arr) {
        int X = jsonObject.getInt("X");
        int Y = jsonObject.getInt("Y");
        int hextype = jsonObject.getInt("hextype");

        HexStone hs = new HexStone(X, Y, hextype);
        sr.addHsArrays(hs, arr);

    }

}