package br.com.brjdevs.bot.core.commands;

import br.com.brjdevs.bot.BRjDevsBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HelpContainer {
    private final Map<ICommand, MessageEmbed> container = new HashMap<>();

    void createHelp(ICommand command) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Help for Command \"" + command.getAliases().get(0) + "\"");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**Aliases:** ").append(String.join(", ", command.getAliases())).append("\n");
        stringBuilder.append("**Descrição:** " + command.getDescription());
        if (command.getExample() != null)
            stringBuilder.append("\n\n**Exemplo:** " + command.getExample());
        embedBuilder.setDescription(stringBuilder.toString());
        embedBuilder.setColor(BRjDevsBot.getGuild().getSelfMember().getColor());
        container.put(command, embedBuilder.build());
    }

    public MessageEmbed getHelp(ICommand command) {
        return container.get(command);
    }

    public Map<ICommand, MessageEmbed> getContainer() {
        return Collections.unmodifiableMap(container);
    }
}
