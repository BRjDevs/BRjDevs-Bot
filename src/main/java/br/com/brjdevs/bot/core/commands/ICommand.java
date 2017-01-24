package br.com.brjdevs.bot.core.commands;

import java.util.List;

public interface ICommand {
	void execute(CommandEvent event, String args);

	List<String> getAliases();

	String getDescription();

	String getExample();

	boolean isAdminCommand();
}
