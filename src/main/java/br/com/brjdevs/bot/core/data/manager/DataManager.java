package br.com.brjdevs.bot.core.data.manager;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.data.objects.Data;
import br.com.brjdevs.bot.core.data.objects.Data.Bot;
import br.com.brjdevs.bot.utils.IOUtils;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.io.File;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static br.com.brjdevs.bot.utils.JSONHelper.getGson;

public class DataManager {
	private Data data;
	private File dataFile;

	public DataManager(String path) {
		try {
			this.dataFile = new File(path);

			if (!dataFile.exists()) {
				BRjDevsBot.LOG.info("Could not find data file, creating a new one...");
				if (dataFile.createNewFile()) {
					BRjDevsBot.LOG.info("Generated new data file at " + dataFile.getPath() + ".");
					IOUtils.write(dataFile, getGson().toJson(new Data()));
				} else {
					BRjDevsBot.LOG.fatal("Could not create data file at " + path);
					BRjDevsBot.LOG.fatal("Exiting application...");
					System.exit(0);
				}
			}

			data = getGson().fromJson(IOUtils.read(dataFile), Data.class);
		} catch (Exception e) {
			BRjDevsBot.LOG.fatal("Something went wrong when initializing the DataManager:");
			BRjDevsBot.LOG.log(e);
			System.exit(0);
		}
	}

	public void checkBots() {
		Set<Member> bots = BRjDevsBot.getGuild().getMembers().stream()
			.filter(member -> member.getUser().isBot() && !getData().bots.containsKey(member.getUser().getId()))
			.collect(Collectors.toSet());

		if (bots.size() == 0) return;

		String botMentions = bots.stream()
			.sorted(Comparator.comparing(Member::getEffectiveName))
			.map(IMentionable::getAsMention)
			.collect(Collectors.joining(" "));

		TextChannel adminChannel = BRjDevsBot.getAdminChannel();
		String prefix = BRjDevsBot.getConfigManager().getConfig().prefix;
		Consumer<Message> deleteAfter = message -> new Thread(() -> {
			try {
				Thread.sleep(16000);
				message.deleteMessage().queue();
			} catch (InterruptedException ignored) {
			}
		}).start();

		adminChannel.sendMessage("Novos Bots na Guild não estão no meu cadastro. Por favor registrá-los usando `" + prefix + "setbotinfo`").queue(deleteAfter);
		adminChannel.sendMessage("**Bots**: " + botMentions).queue(deleteAfter);
	}

	public void cleanupBots() {
		if (data.bots.keySet().removeIf(s -> BRjDevsBot.getGuild().getMemberById(s) == null)) update();
	}

	public Data getData() {
		return data;
	}

	public boolean isBotOwner(Member bot, Member user) {
		if (!bot.getUser().isBot()) return false;
		Bot botInfo = BRjDevsBot.getDataManager().getData().bots.get(bot.getUser().getId());
		return botInfo != null && botInfo.owners.contains(user.getUser().getId());
	}

	public void update() {
		try {
			IOUtils.write(dataFile, getGson().toJson(data));
		} catch (Exception e) {
			BRjDevsBot.LOG.info("An error occurred when updating Data!");
			BRjDevsBot.LOG.log(e);
		}
	}
}
