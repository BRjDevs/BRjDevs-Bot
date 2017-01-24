package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.utils.StringUtils;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.lang.management.ManagementFactory;

public class Session {
	private final Runtime instance = Runtime.getRuntime();
	private final int mb = 1024 * 1024;
	public long cmds;
	public double cpuUsage;

	public Session() {
		this.cpuUsage = 0;
		this.cmds = 0;
	}

	public String getUptime() {
		final long
			duration = ManagementFactory.getRuntimeMXBean().getUptime(),
			years = duration / 31104000000L,
			months = duration / 2592000000L % 12,
			days = duration / 86400000L % 30,
			hours = duration / 3600000L % 24,
			minutes = duration / 60000L % 60,
			seconds = duration / 1000L % 60;
		String uptime = (years == 0 ? "" : years + " Years, ") + (months == 0 ? "" : months + " Months, ")
			+ (days == 0 ? "" : days + " Days, ") + (hours == 0 ? "" : hours + " Hours, ")
			+ (minutes == 0 ? "" : minutes + " Minutes, ") + (seconds == 0 ? "" : seconds + " Seconds, ");

		uptime = StringUtils.replaceLast(uptime, ", ", "");
		return StringUtils.replaceLast(uptime, ",", " and");
	}

	public MessageEmbed toEmbed(CommandEvent event) {
		String ram = ((instance.totalMemory() - instance.freeMemory()) / mb) + " MB/" + (instance.totalMemory() / mb) + " MB";
		JDA jda = event.getJDA();

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setAuthor("Bot Stats (" + Utils.getUser(jda.getSelfUser()) + ")", null, jda.getSelfUser().getAvatarUrl());
		embedBuilder.addField("Uptime", getUptime(), false);
		embedBuilder.addField("Threads", String.valueOf(Thread.activeCount()), true);
		embedBuilder.addField("RAM (USAGE/MAX)", String.valueOf(ram), true);
		embedBuilder.addField("CPU Usage", String.valueOf(cpuUsage) + "%", true);
		embedBuilder.addField("JDA Version", JDAInfo.VERSION, true);
		embedBuilder.addField("API Responses", jda.getResponseTotal() + "", true);
		Color color = BRjDevsBot.getGuild().getSelfMember().getColor();
		embedBuilder.setColor(color == null ? Color.decode("#F1AC1A") : color);

		return embedBuilder.build();
	}

}
