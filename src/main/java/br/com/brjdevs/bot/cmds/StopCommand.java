package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;

import java.util.Arrays;
import java.util.List;

public class StopCommand implements ICommand {
    @Override
    public void execute(CommandEvent event, String args) {
        BRjDevsBot.getDataManager().update();
        BRjDevsBot.getConfigManager().update();
        event.reply(":wave:").complete();
        event.getJDA().shutdown();
        BRjDevsBot.LOG.info("Stopping application...");
        System.exit(0);
    }
    @Override
    public boolean isAdminCommand() {
        return true;
    }
    @Override
    public String getDescription() {
        return "Me desliga!";
    }
    @Override
    public String getExample() {
        return null;
    }
    @Override
    public List<String> getAliases() {
        return Arrays.asList("stop", "shutdown");
    }
}
