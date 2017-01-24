package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.data.objects.Data.Cases;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

public class ModLog {

	public enum ModAction {
		TEMP_BAN, BAN, KICK
	}

	public static void log(Member author, Member target, String reason, ModAction action, String... time) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.addField("Responsible Moderator", Utils.getUser(author.getUser()), true);
		Cases cases = BRjDevsBot.getDataManager().getData().cases;
		embedBuilder.addField("Member", Utils.getUser(target.getUser()), true);
		embedBuilder.addField("Reason", reason, false);
		embedBuilder.setThumbnail(target.getUser().getEffectiveAvatarUrl());
		switch (action) {
			case BAN:
				embedBuilder.setAuthor("Ban | Case #" + cases.ban, null, author.getUser().getEffectiveAvatarUrl());
				break;
			case TEMP_BAN:
				embedBuilder.setAuthor("Temp Ban | Case #" + cases.tempban, null, author.getUser().getEffectiveAvatarUrl());
				embedBuilder.addField("Time", time[0], true);
				break;
			case KICK:
				embedBuilder.setAuthor("Kick | Case #" + cases.kick, null, author.getUser().getEffectiveAvatarUrl());
				break;
		}
		BRjDevsBot.getLogChannel().sendMessage(embedBuilder.build()).queue();
	}

	public static void logUnban(Member author, String target, String reason) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.addField("Responsible Moderator", Utils.getUser(author.getUser()), true);
		embedBuilder.addField("Member ID", target, true);
		embedBuilder.addField("Reason", reason, false);
		embedBuilder.setAuthor("Unban", null, author.getUser().getEffectiveAvatarUrl());
		BRjDevsBot.getLogChannel().sendMessage(embedBuilder.build()).queue();
	}
}
