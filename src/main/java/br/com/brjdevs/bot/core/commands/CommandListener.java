package br.com.brjdevs.bot.core.commands;

import br.com.brjdevs.bot.BRjDevsBot;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class CommandListener implements EventListener {
	@Override
	public void onEvent(Event e) {
		if (e instanceof MessageReceivedEvent) {
			MessageReceivedEvent event = (MessageReceivedEvent) e;
			if (event.getAuthor().isBot() || event.getAuthor().isFake()) return;
			if (!event.isFromType(ChannelType.TEXT)) {
				event.getChannel().sendTyping().queue(success ->
					event.getChannel().sendMessage("I am only able to run Commands in TextChannels!").queue());
				return;
			}
			String prefix = BRjDevsBot.getConfigManager().getConfig().prefix;
			String message = event.getMessage().getRawContent();
			if (message.startsWith(prefix) && message.length() > prefix.length() + 1) {
				String baseCmd = message.substring(prefix.length()).split(" ")[0].trim();
				ICommand command = BRjDevsBot.getCommandManager().get(baseCmd);
				if (command == null) return;
				if (command.isAdminCommand() && !BRjDevsBot.getAdmins().contains(event.getMember())) {
					event.getChannel().sendTyping().queue(success ->
						event.getChannel().sendMessage("You can't use this Command.").queue());
					return;
				}
				command.execute(new CommandEvent(event), message.substring(prefix.length() + baseCmd.length()).trim());
			}
		}
	}
}
