package br.com.brjdevs.bot.core.data.objects;

public class Config {
	public static class Game {
		public String name = "";
		public boolean stream = false;
	}

	public String admin_channel = "195960925134520320";
	public String admin_role = "195960797632004096";
	public Game game = new Game();
	public String log_channel = "271433149345562634";
	public String prefix = "$> ";
	public String token = "";
}
