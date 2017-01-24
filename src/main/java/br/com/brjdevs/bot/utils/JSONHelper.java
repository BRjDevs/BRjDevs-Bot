package br.com.brjdevs.bot.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JSONHelper {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static Gson getGson() {
		return GSON;
	}

	public static String prettyPrint(String json) {
		return prettyPrint(new JsonParser().parse(json));
	}

	public static String prettyPrint(JsonElement parse) {
		return GSON.toJson(parse);
	}
}
