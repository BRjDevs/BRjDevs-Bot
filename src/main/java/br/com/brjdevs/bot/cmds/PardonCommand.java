package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.ModLog;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

public class PardonCommand implements ICommand {
    @Override
    public void execute(CommandEvent event, String args) {
        String[] splitArgs = StringUtils.splitArgs(args, 2);
        String id = splitArgs[0];
        event.getGuild().getController().unban(id).queue(success -> {
            String reason = splitArgs[1];
            ModLog.logunBan(event.getMember(), id, reason);
            BRjDevsBot.getTempBanManager().removeTempban(id);
            event.reply("Done! User was unbanned!");
        }, failure -> {
            if (failure.getMessage().contains("does not exist!")) {
                event.reply("This user doesn't exist!").queue();
            } else {
                event.reply("This user is not banned!").queue();
            }
        });
    }
    @Override
    public boolean isAdminCommand() {
        return false;
    }
    @Override
    public String getDescription() {
        return "Tira qualquer punição que esteja sendo aplicada no usuário.";
    }
    @Override
    public String getExample() {
        return "pardon <@268824727450025991>";
    }
    @Override
    public List<String> getAliases() {
        return Arrays.asList("pardon", "unban");
    }
}
