package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class InCaseOfFireListener implements EventListener {
	@Override
	public void onEvent(Event event) {
		if (event instanceof ReconnectedEvent || event instanceof ResumedEvent) {
			BRjDevsBot.setupStaticLinking();
		} else if (event instanceof GuildJoinEvent) {
			BRjDevsBot.kickMyselfOutOfMultipleGuilds();
		} else if (event instanceof GuildLeaveEvent && event.getJDA().getGuilds().size() == 0) {
			BRjDevsBot.LOG.fatal("Okay, I'm out.");
			System.exit(0);
		}
	}
}
