package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuildInfoCommand implements ICommand {
	@Override
	public void execute(CommandEvent event, String args) {
		Guild guild = BRjDevsBot.getGuild();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setThumbnail(guild.getIconUrl());
		embedBuilder.setAuthor(guild.getName(), null, guild.getIconUrl());
		embedBuilder.addField("Owner", Utils.getUser(guild.getOwner().getUser()), true);
		embedBuilder.addField("Admins", String.join("\n", BRjDevsBot.getAdmins().stream().map(member -> Utils.getUser(member.getUser())).collect(Collectors.toList())), true);
		List<Member> online = guild.getMembers().stream().filter(member -> member.getOnlineStatus() == OnlineStatus.ONLINE).collect(Collectors.toList());
		embedBuilder.addField("Members (TOTAL/ONLINE)", guild.getMembers().size() + "/" + online.size(), true);
		embedBuilder.addField("Roles", guild.getRoles().size() + "", true);
		embedBuilder.addField("Text Channels", guild.getTextChannels().size() + "", true);
		embedBuilder.addField("Voice Channels", guild.getVoiceChannels().size() + "", true);
		embedBuilder.setColor(guild.getSelfMember().getColor());

		event.reply(embedBuilder.build()).queue();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("guild", "guildinfo");
	}

	@Override
	public String getDescription() {
		return "Da informação sobre a Guild!";
	}

	@Override
	public String getExample() {
		return null;
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}
}
