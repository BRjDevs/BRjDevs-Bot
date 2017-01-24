package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;

import java.util.Arrays;
import java.util.List;

public class StatusCommand implements ICommand {
	@Override
	public void execute(CommandEvent event, String args) {
		event.reply(BRjDevsBot.getSession().toEmbed(event)).queue();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("stats", "status");
	}

	@Override
	public String getDescription() {
		return "Mostra os meus status!";
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
