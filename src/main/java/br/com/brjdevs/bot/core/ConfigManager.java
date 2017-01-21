package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.utils.IOUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private File configFile;
    private JsonObject obj;

    public ConfigManager(String file) {
        try {
            configFile = new File(file);

            if (!configFile.exists()) {
                BRjDevsBot.LOG.info("Could not find config file, creating a new one...");
                if (configFile.createNewFile()) {
                    BRjDevsBot.LOG.info("Generated new config file at " + configFile.getPath() + ".");
                    IOUtils.write(configFile, getDefaultConfigObject().toString());
                    BRjDevsBot.LOG.info("Please, fill the file with valid properties.");
                } else {
                    BRjDevsBot.LOG.fatal("Could not create config file at " + file);
                }
                System.exit(0);
            }

            String data = IOUtils.read(configFile);
            obj = new JsonParser().parse(data).getAsJsonObject();

        } catch (Exception e) {
            BRjDevsBot.LOG.fatal("Something went wrong when initializing the ConfigLoader:");
            BRjDevsBot.LOG.log(e);
            System.exit(0);
        }
    }

    public JsonObject getConfig() {
        return obj;
    }

    public void update() {
        try {
            IOUtils.write(configFile, obj.toString());
        } catch (Exception e) {
            BRjDevsBot.LOG.fatal("Something went wrong when updating the config file!");
            BRjDevsBot.LOG.log(e);
        }
    }

    private static JsonObject getDefaultConfigObject() {
        JsonObject config = new JsonObject();
        config.addProperty("token", "");
        config.addProperty("prefix", "$> ");
        config.addProperty("log-channel", "271433149345562634");
        config.addProperty("admin-role", "195960797632004096");
        JsonObject gameObj = new JsonObject();
        gameObj.addProperty("stream", false);
        gameObj.addProperty("name", "");
        config.add("game", gameObj);
        return config;
    }
}
