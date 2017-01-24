package br.com.brjdevs.bot.core.data.manager;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.data.objects.Config;
import br.com.brjdevs.bot.utils.IOUtils;

import java.io.File;

import static br.com.brjdevs.bot.utils.JSONHelper.getGson;

public class ConfigManager {
	private Config config;
	private File configFile;

	public ConfigManager(String file) {
		try {
			configFile = new File(file);

			if (!configFile.exists()) {
				BRjDevsBot.LOG.info("Could not find config file, creating a new one...");
				if (configFile.createNewFile()) {
					BRjDevsBot.LOG.info("Generated new config file at " + configFile.getPath() + ".");
					IOUtils.write(configFile, getGson().toJson(new Config()));
					BRjDevsBot.LOG.info("Please, fill the file with valid properties.");
				} else {
					BRjDevsBot.LOG.fatal("Could not create config file at " + file);
				}
				System.exit(0);
			}

			config = getGson().fromJson(IOUtils.read(configFile), Config.class);
		} catch (Exception e) {
			BRjDevsBot.LOG.fatal("Something went wrong when initializing the ConfigLoader:");
			BRjDevsBot.LOG.log(e);
			System.exit(0);
		}
	}

	public Config getConfig() {
		return config;
	}

	public void update() {
		try {
			IOUtils.write(configFile, getGson().toJson(config));
		} catch (Exception e) {
			BRjDevsBot.LOG.fatal("Something went wrong when updating the config file!");
			BRjDevsBot.LOG.log(e);
		}
	}
}
