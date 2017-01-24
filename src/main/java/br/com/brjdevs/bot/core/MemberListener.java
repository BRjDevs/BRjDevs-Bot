package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.ArrayList;
import java.util.List;

public class MemberListener implements EventListener {
	private static List<String> ignored = new ArrayList<>();

	public static void ignoreLeave(String userId) {
		ignored.add(userId);
	}

	public static void send(Member member, boolean leave) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(Utils.getUser(member.getUser()) + (leave ? " has left the guild!" : " has joined the guild!"), null, BRjDevsBot.getGuild().getIconUrl());
		builder.setDescription(
			"**User ID:** " + member.getUser().getId() + "");
		builder.setFooter("Total Members: " + BRjDevsBot.getGuild().getMembers().size(), null);
		builder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
		BRjDevsBot.getLogChannel().sendMessage(builder.build()).queue();
	}

	@Override
	public void onEvent(Event e) {
		if (e instanceof GuildMemberLeaveEvent) {
			GuildMemberLeaveEvent event = (GuildMemberLeaveEvent) e;
			if (ignored.remove(event.getMember().getUser().getId())) return;
			send(event.getMember(), true);
			if (event.getMember().getUser().isBot()) BRjDevsBot.getDataManager().cleanupBots();
		} else if (e instanceof GuildMemberJoinEvent) {
			GuildMemberJoinEvent event = (GuildMemberJoinEvent) e;
			send(event.getMember(), false);
			if (event.getMember().getUser().isBot()) BRjDevsBot.getDataManager().checkBots();
		}
	}
}
