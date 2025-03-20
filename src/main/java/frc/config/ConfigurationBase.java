package frc.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**  class that manages the robot's JSON-based persistant configuration */
class ConfigurationBase {
    
    private static final JsonFactory jsonFactory = new JsonFactory();

    public static final File DEFAULT_FILE = new File("\\home\\lvuser\\robot-config.json");

    private static boolean initFlag = false;

    private static final HashMap<String, Double> values = new HashMap<>();

    private static final HashMap<String, GenericEntry> entries = new HashMap<>();

    public static void load(File f) {

        try {
            
            final JsonParser jsonParse = jsonFactory.createParser(f);

            do {
                
                while (!jsonParse.nextToken().isNumeric() && jsonParse.currentToken() != null) {
                    // skip
                }

                final String key = jsonParse.getCurrentName();

                values.put(key, jsonParse.getDoubleValue());

                GenericEntry e = tabOf(key).add(nameOf(key), values.get(key)).getEntry();

                entries.put(key, e);
                
            }
            while (jsonParse.currentToken() != null);

            initFlag = true;
        }

        catch (Exception e) {
            System.out.println("JSON configuration error enountereed. Falling back to hard coded values.");
            e.printStackTrace();
        }
    }

    /* package-private */ static double getOr(String key, double default_) {

        if (values.containsKey(key) && initFlag && !Double.isNaN(values.get(key))) {
            return values.get(key);
        }
        else {
            values.put(key, default_);
        }

        return default_;
    }

    /* package-private */ static void save() throws IOException {

        final JsonGenerator jsonGen = jsonFactory.createGenerator(DEFAULT_FILE, JsonEncoding.UTF8);

        jsonGen.writeStartObject();

        for (var key : values.keySet()) {

            jsonGen.writeNumberField(key, values.get(key));
        }

        jsonGen.writeEndObject();
    }

    /* package-private */ static void refresh() {

        for (String key : entries.keySet()) {
            values.put(key, entries.get(key).getDouble(Double.NaN));
        }

        try {
            save();
        }
        catch (IOException e) {
            System.out.println("Failed to save configuraion:");
            
            e.printStackTrace();
        }
    }

    private static final ShuffleboardTab tabOf(String key) {
        return Shuffleboard.getTab(key.substring(0, key.indexOf(".")));
    }

    private static final String nameOf(String key) {
        return key.substring(key.indexOf(".") + 1);
    }
}
