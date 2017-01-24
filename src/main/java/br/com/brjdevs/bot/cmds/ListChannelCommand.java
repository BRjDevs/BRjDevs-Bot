package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListChannelCommand implements ICommand {
	@Override
	public void execute(CommandEvent event, String args) {
		List<TextChannel> channels = BRjDevsBot.getDataManager().getData().public_channels.stream()
			.map(s -> event.getJDA().getTextChannelById(s)).filter(Objects::nonNull).collect(Collectors.toList());

		event.reply(String.format(
			"```md\n# Canais publicados:\n%s\n# Total de canais: %d```",
			channels.stream()
				.map(channel -> String.format("%d. %s", channels.indexOf(channel) + 1, channel.getName()))
				.collect(Collectors.joining("\n")), channels.size())).queue();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("channels", "canais");
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
	public boolean isAdminCommand() {
		return false;
	}
}
