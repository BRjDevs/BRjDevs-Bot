package br.com.brjdevs.bot.core.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.RestAction;

public class CommandEvent {
	private MessageReceivedEvent event;

	public CommandEvent(MessageReceivedEvent event) {
		this.event = event;
	}

	public User getAuthor() {
		return event.getAuthor();
	}

	public TextChannel getChannel() {
		return event.getTextChannel();
	}

	public Guild getGuild() {
		return event.getGuild();
	}

	public JDA getJDA() {
		return event.getJDA();
	}

	public Member getMember() {
		return event.getMember();
	}

	public Message getMessage() {
		return event.getMessage();
	}

	public RestAction<Message> reply(String message) {
		event.getChannel().sendTyping().complete();
		return event.getChannel().sendMessage(message);
	}

	public RestAction<Message> reply(Message message) {
		return reply(message.getRawContent());
	}

	public RestAction<Message> reply(MessageEmbed embed) {
		event.getChannel().sendTyping().complete();
		return event.getChannel().sendMessage(embed);
	}
}
