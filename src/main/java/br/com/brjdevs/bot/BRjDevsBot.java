package br.com.brjdevs.bot;

import br.com.brjdevs.bot.core.InCaseOfFireListener;
import br.com.brjdevs.bot.core.MemberListener;
import br.com.brjdevs.bot.core.Session;
import br.com.brjdevs.bot.core.TempBanManager;
import br.com.brjdevs.bot.core.commands.CommandListener;
import br.com.brjdevs.bot.core.commands.CommandManager;
import br.com.brjdevs.bot.core.data.manager.ConfigManager;
import br.com.brjdevs.bot.core.data.manager.DataManager;
import br.com.brjdevs.bot.core.data.objects.Config;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.util.List;

public class BRjDevsBot {
	public static SimpleLog LOG;
	private static TextChannel adminChannel;
	private static Role adminRole;
	private static CommandManager commandManager;
	private static ConfigManager configManager;
	private static DataManager dataManager;
	private static Guild guild;
	private static JDA jda;
	private static TextChannel logChannel;
	private static Session session;
	private static TempBanManager unbans;

	static {
		LOG = SimpleLog.getLog("BRjDevs Bot");
	}

	public static TextChannel getAdminChannel() {
		return adminChannel;
	}

	public static Role getAdminRole() {
		return adminRole;
	}

	public static List<Member> getAdmins() {
		return guild.getMembersWithRoles(adminRole);
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public static ConfigManager getConfigManager() {
		return configManager;
	}

	public static DataManager getDataManager() {
		return dataManager;
	}

	public static Guild getGuild() {
		return guild;
	}

	public static JDA getJDA() {
		return jda;
	}

	public static TextChannel getLogChannel() {
		return logChannel;
	}

	public static Session getSession() {
		return session;
	}

	public static TempBanManager getTempBanManager() {
		return unbans;
	}

	public static void kickMyselfOutOfMultipleGuilds() {
		if (jda.getGuilds().size() != 1) {
			LOG.fatal("WTF I'M ON TWO GUILDS?\n" + jda.getGuilds());
			jda.getGuilds().stream().filter(g -> !g.getId().equals("178878447614033921")).forEach(Guild::leave);
		}
	}

	public static void main(String[] args) {
		try {
			configManager = new ConfigManager(System.getProperty("user.dir") + "/config.json");
			JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT)
				.setToken(configManager.getConfig().token)
				.addListener(new InCaseOfFireListener(), new CommandListener(), new MemberListener());
			Config.Game game = configManager.getConfig().game;
			if (!game.name.isEmpty())
				jdaBuilder.setGame(game.stream ? Game.of(game.name, "https://twitch.tv/ ") : Game.of(game.name));

			jda = jdaBuilder.buildBlocking();

			kickMyselfOutOfMultipleGuilds();
			setupStaticLinking();

			commandManager = new CommandManager();
			commandManager.load();
			session = new Session();
			dataManager = new DataManager(System.getProperty("user.dir") + "/data.json");
			unbans = new TempBanManager(dataManager.getData().tempbans);
			dataManager.checkBots();
			LOG.info("Finished loading!");
		} catch (Exception e) {
			LOG.fatal("Something went wrong:");
			LOG.log(e);
			System.exit(0);
		}
	}

	public static void setupStaticLinking() {
		logChannel = jda.getTextChannelById(configManager.getConfig().log_channel);
		if (logChannel == null) {
			LOG.fatal("I can't initialize because the given \"log-channel\" doesn't exists or I can't see it.");
			System.exit(0);
		}
		if (!logChannel.canTalk()) {
			LOG.fatal("I can't talk in \"" + logChannel.getName() + "\"!");
			System.exit(0);
		}
		adminChannel = jda.getTextChannelById(configManager.getConfig().admin_channel);
		if (adminChannel == null) {
			LOG.fatal("I can't initialize because the given \"admin-channel\" doesn't exists or I can't see it.");
			System.exit(0);
		}
		if (!adminChannel.canTalk()) {
			LOG.fatal("I can't talk in \"" + logChannel.getName() + "\"!");
			System.exit(0);
		}
		guild = jda.getGuildById("178878447614033921");
		adminRole = guild.getRoleById(configManager.getConfig().admin_role);
	}
}
