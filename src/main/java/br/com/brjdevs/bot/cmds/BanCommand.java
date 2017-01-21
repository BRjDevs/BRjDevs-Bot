package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.ModLog;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.Utils;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BanCommand implements ICommand {
    @Override
    public void execute(CommandEvent event, String args) {
        String reason = args;
        if (event.getMessage().getMentionedUsers().isEmpty()) {
            event.reply("Você tem que mencionar um usuário!").queue();
            return;
        }
        for (User user : event.getMessage().getMentionedUsers()) {
            reason = reason.replaceAll("(\\s+)?<@!?" + user.getId() + ">(\\s+)?", "");
        }
        if (reason.isEmpty()) {
            event.reply("Você não pode banir um usuário sem um motivo!").queue();
            return;
        }
        JsonObject cases = BRjDevsBot.getDataManager().getData().get("cases").getAsJsonObject();
        String fReason = reason;
        event.getMessage().getMentionedUsers().forEach(user -> {
            if (user == event.getJDA().getSelfUser()) {
                event.reply("Você não pode me banir!").queue();
                return;
            }
            user.openPrivateChannel().complete().sendMessage("Você foi banido por " + event.getAuthor().getAsMention() + ".\nMotivo: " + fReason).complete();
            cases.addProperty("ban", cases.get("ban").getAsInt() + 1);
            event.getGuild().getController().ban(user, 7).queue();
            ModLog.log(event.getMember(), event.getGuild().getMember(user), fReason, ModLog.ModAction.BAN);
        });
        BRjDevsBot.getDataManager().update();
        event.reply("The Ban Hammer has spoken! Banned " + (String.join(", ", event.getMessage().getMentionedUsers().stream().map(Utils::getUser).collect(Collectors.toList()))) + ".").queue();
    }
    @Override
    public boolean isAdminCommand() {
        return true;
    }
    @Override
    public String getDescription() {
        return "Bane um usuário!";
    }
    @Override
    public String getExample() {
        return "ban <@268824727450025991> Exemplo";
    }
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("ban");
    }
}
