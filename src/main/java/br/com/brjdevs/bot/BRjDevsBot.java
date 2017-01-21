package br.com.brjdevs.bot;

import br.com.brjdevs.bot.core.DataManager;
import br.com.brjdevs.bot.core.Session;
import br.com.brjdevs.bot.core.TempBanManager;
import br.com.brjdevs.bot.core.commands.CommandListener;
import br.com.brjdevs.bot.core.commands.CommandManager;
import br.com.brjdevs.bot.core.ConfigManager;
import br.com.brjdevs.bot.utils.Utils;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BRjDevsBot {
    private static JDA jda;
    public static SimpleLog LOG;
    private static ConfigManager configManager;
    private static CommandManager commandManager;
    private static TextChannel logChannel;
    private static Guild guild;
    private static Role adminRole;
    private static DataManager dataManager;
    private static Session session;
    private static TempBanManager unbans;

    static {
        LOG = SimpleLog.getLog("BRjDevs Bot");
    }

    public static void main(String[] args) {
        try {
            configManager = new ConfigManager(System.getProperty("user.dir") + "/config.json");
            JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT)
                    .setToken(configManager.getConfig().get("token").getAsString())
                    .addListener(new CommandListener());
            JsonObject gameObj = configManager.getConfig().get("game").getAsJsonObject();
            String name = gameObj.get("name").getAsString();
            if (!Utils.isEmpty(name)) {
                jdaBuilder.setGame(gameObj.get("stream").getAsBoolean() ? Game.of(name, "https://twitch.tv/ ") : Game.of(name));
            }
            jda = jdaBuilder.buildBlocking();
            if (jda.getGuilds().size() != 1) {
                LOG.fatal("WTF I'M ON TWO GUILDS?\n" + jda.getGuilds());
                System.exit(0);
            }
            logChannel = jda.getTextChannelById(configManager.getConfig().get("log-channel").getAsString());
            if (logChannel == null) {
                LOG.fatal("I can't initialize because the given \"log-channel\" doesn't exists or I can't see it.");
                System.exit(0);
            }
            if (!logChannel.canTalk()) {
                LOG.fatal("I can't talk in \"" + logChannel.getName() + "\"!");
                System.exit(0);
            }
            guild = jda.getGuildById("178878447614033921");
            adminRole = guild.getRoleById(configManager.getConfig().get("admin-role").getAsString());
            commandManager = new CommandManager();
            commandManager.load();
            session = new Session();
            dataManager = new DataManager(System.getProperty("user.dir") + "/data.json");
            Map<String, Long> map = new HashMap<>();
            dataManager.getData().get("tempbans")
                    .getAsJsonObject().entrySet().forEach(entry -> {
                        map.put(entry.getKey(), entry.getValue().getAsLong());
            });
            unbans = new TempBanManager(map);
            LOG.info("Finished loading!");
        } catch (Exception e) {
            LOG.fatal("Something went wrong:");
            LOG.log(e);
        }
    }

    public static JDA getJDA() {
        return jda;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static TextChannel getLogChannel() {
        return logChannel;
    }

    public static Guild getGuild() {
        return guild;
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

    public static DataManager getDataManager() {
        return dataManager;
    }

    public static Session getSession() {
        return session;
    }

    public static TempBanManager getTempBanManager() {
        return unbans;
    }
}
