package br.com.brjdevs.bot.core.commands;

import java.util.List;

public interface ICommand {
    void execute(CommandEvent event, String args);
    boolean isAdminCommand();
    String getDescription();
    String getExample();
    List<String> getAliases();
}
