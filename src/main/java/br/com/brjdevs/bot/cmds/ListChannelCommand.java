package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import com.google.gson.JsonElement;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListChannelCommand implements ICommand {
    @Override
    public void execute(CommandEvent event, String args) {
        String string;
        List<TextChannel> textChannels = new ArrayList<>();
        for (JsonElement element : BRjDevsBot.getDataManager().getData().get("public_channels").getAsJsonArray()) {
            TextChannel channel = event.getJDA().getTextChannelById(element.getAsString());
            if (channel != null)
                textChannels.add(channel);
        }
        string = String.join("\n", textChannels.stream().map(channel -> (textChannels.indexOf(channel) + 1) + ". " + channel.getName()).collect(Collectors.toList()));
        string = "```md\n# Canais publicados:\n" + string + "\n# Total de canais: " + textChannels.size() + "```";
        event.reply(string).queue();
    }
    @Override
    public boolean isAdminCommand() {
        return false;
    }
    @Override
    public String getDescription() {
        return "Lista todos os canais publicados!";
    }
    @Override
    public String getExample() {
        return null;
    }
    @Override
    public List<String> getAliases() {
        return Arrays.asList("channels", "canais");
    }
}
