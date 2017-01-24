package br.com.brjdevs.bot.core.data.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Data {
	public static class Bot {
		public boolean hasMusic = false, isDevBot = false;
		public Set<String> owners = new HashSet<>();
		public String prefix = "", description = "";
	}

	public static class Cases {
		public int ban = 0, tempban = 0, kick = 0;
	}

	public Map<String, Bot> bots = new HashMap<>();
	public Cases cases = new Cases();
	public Set<String> public_channels = new HashSet<>();
	public Map<String, Long> tempbans = new HashMap<>();
}
