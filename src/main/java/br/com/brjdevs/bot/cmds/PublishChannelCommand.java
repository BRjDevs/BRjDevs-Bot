package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collections;
import java.util.List;

public class PublishChannelCommand implements ICommand {
	@Override
	public void execute(CommandEvent event, String args) {
		if (event.getMessage().getMentionedChannels().size() != 1) {
			event.reply("Você tem que mencionar **somente** um canal para usar esse comando.").queue();
			return;
		}
		TextChannel channel = event.getMessage().getMentionedChannels().get(0);
		if (BRjDevsBot.getDataManager().getData().public_channels.contains(channel.getId())) {
			event.reply(channel.getAsMention() + " já está publicado!").queue();
			return;
		}
		BRjDevsBot.getDataManager().getData().public_channels.add(channel.getId());
		BRjDevsBot.getDataManager().update();
		event.reply(channel.getAsMention() + " foi publicado! Usuários podem ter acesso ao canal usando `$> show " + channel.getName() + "`.").queue();
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("publish");
	}

	@Override
	public String getDescription() {
		return "Publica um canal!";
	}

	@Override
	public String getExample() {
		return "publish <#208395890707136514>";
	}

	@Override
	public boolean isAdminCommand() {
		return true;
	}
}
