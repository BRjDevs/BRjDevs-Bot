package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import com.google.gson.JsonPrimitive;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collections;
import java.util.List;

public class UnpublishCommand implements ICommand {
    @Override
    public void execute(CommandEvent event, String args) {
        if (event.getMessage().getMentionedChannels().size() != 1) {
            event.reply("Você tem que mencionar **somente** um canal para usar esse comando.").queue();
            return;
        }
        TextChannel channel = event.getMessage().getMentionedChannels().get(0);
        if (!BRjDevsBot.getDataManager().getData().get("public_channels").getAsJsonArray().contains(new JsonPrimitive(channel.getId()))) {
            event.reply(channel.getAsMention() + " não está publicado!").queue();
            return;
        }
        BRjDevsBot.getDataManager().getData().get("public_channels").getAsJsonArray().remove(new JsonPrimitive(channel.getId()));
        BRjDevsBot.getDataManager().update();
        event.reply(channel.getAsMention() + " foi despublicado! Usuários não podem mais ter acesso a esse canal por mim!").queue();
    }
    @Override
    public boolean isAdminCommand() {
        return true;
    }
    @Override
    public String getDescription() {
        return "Despublica um canal!";
    }
    @Override
    public String getExample() {
        return "unpublish <#208395890707136514>";
    }
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("unpublish");
    }
}
