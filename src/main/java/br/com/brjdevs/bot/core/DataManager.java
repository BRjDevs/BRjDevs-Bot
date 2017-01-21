package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.utils.IOUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;

public class DataManager {
    private JsonObject data;
    private File dataFile;

    public DataManager(String path) {
        try {
            this.dataFile = new File(path);

            if (!dataFile.exists()) {
                BRjDevsBot.LOG.info("Could not find data file, creating a new one...");
                if (dataFile.createNewFile()) {
                    BRjDevsBot.LOG.info("Generated new data file at " + dataFile.getPath() + ".");
                    IOUtils.write(dataFile, getDefaultDataObject().toString());
                } else {
                    BRjDevsBot.LOG.fatal("Could not create data file at " + path);
                    BRjDevsBot.LOG.fatal("Exiting application...");
                    System.exit(0);
                }
            }
            String data = IOUtils.read(dataFile);
            this.data = new JsonParser().parse(data).getAsJsonObject();
        } catch (Exception e) {
            BRjDevsBot.LOG.fatal("Something went wrong when initializing the DataManager:");
            BRjDevsBot.LOG.log(e);
            System.exit(0);
        }
    }

    public JsonObject getData() {
        return data;
    }

    public void update() {
        try {
            IOUtils.write(dataFile, data.toString());
        } catch (Exception e) {
            BRjDevsBot.LOG.info("An error occurred when updating Data!");
            BRjDevsBot.LOG.log(e);
        }
    }

    private static JsonObject getDefaultDataObject() {
        JsonObject object = new JsonObject();
        object.add("public_channels", new JsonArray());
        JsonObject cases = new JsonObject();
        cases.addProperty("ban", 0);
        cases.addProperty("tempban", 0);
        cases.addProperty("kick", 0);
        object.add("cases", cases);
        object.add("tempbans", new JsonObject());
        return object;
    }
}
