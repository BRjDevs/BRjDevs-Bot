package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.ChannelUtils;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public class HideCommand implements ICommand {
	@Override
	public void execute(CommandEvent event, String args) {
		List<TextChannel> results = event.getMessage().getMentionedChannels().isEmpty() ?
			BRjDevsBot.getGuild().getTextChannelsByName(args, true) : event.getMessage().getMentionedChannels();
		if (results.isEmpty()) {
			event.reply("Oops, não achei nenhum canal com o nome `" + args + "`.").queue();
			return;
		}
		TextChannel textChannel = results.get(0);
		if (!textChannel.canTalk(event.getMember())) {
			event.reply("Você já não tem permissão para falar nesse canal!").queue();
			return;
		}
		if (!BRjDevsBot.getDataManager().getData().public_channels.contains(textChannel.getId())) {
			event.reply("Esse canal não foi publicado.").queue();
			return;
		}
		Member member = !event.getMessage().getMentionedUsers().isEmpty() ? event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)) : event.getMember();
		if (!member.equals(event.getMember()) && !BRjDevsBot.getAdmins().contains(event.getMember())) {
			event.reply("Você só pode fazer isso em si mesmo!").queue();
			return;
		}
		ChannelUtils.hide(textChannel, member);
		event.reply("Feito! Agora " + (member.equals(event.getMember()) ? "você" : Utils.getUser(member.getUser())) + " não pode falar em " + textChannel.getName() + "!").queue();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("hide", "esconder");
	}

	@Override
	public String getDescription() {
		return "Tira sua permissão de ver um canal!";
	}

	@Override
	public String getExample() {
		return "esconder <#229061741517078528>";
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}
}
