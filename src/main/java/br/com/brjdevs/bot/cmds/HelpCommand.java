package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class HelpCommand implements ICommand {
    @Override
    public void execute(CommandEvent event, String args) {
        if (args.isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Command List");
            embedBuilder.setColor(event.getGuild().getSelfMember().getColor());
            BRjDevsBot.getCommandManager().getCommands()
                    .forEach(cmd ->
                            embedBuilder.addField(cmd.getAliases().get(0), cmd.getDescription(), true));
            embedBuilder.setColor(BRjDevsBot.getGuild().getSelfMember().getColor());
            event.reply(embedBuilder.build()).queue();
        } else {
            ICommand command = BRjDevsBot.getCommandManager().get(args);
            if (command == null) {
                event.reply("Nenhum comando encontrado.").queue();
                return;
            }
            event.reply(BRjDevsBot.getCommandManager().getHelpContainer().getHelp(command)).queue();
        }
    }
    @Override
    public boolean isAdminCommand() {
        return false;
    }
    @Override
    public String getDescription() {
        return "Te envia informações sobre meus comandos!";
    }
    @Override
    public String getExample() {
        return null;
    }
    @Override
    public List<String> getAliases() {
        return Arrays.asList("help", "ajuda");
    }
}
