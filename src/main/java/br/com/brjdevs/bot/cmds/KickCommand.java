package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.MemberListener;
import br.com.brjdevs.bot.core.ModLog;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KickCommand implements ICommand {

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
			event.reply("Você não pode kickar um usuário sem um motivo!").queue();
			return;
		}
		String fReason = reason;
		event.getMessage().getMentionedUsers().forEach(user -> {
			if (user == event.getJDA().getSelfUser()) {
				event.reply("Você não pode me kickar!").queue();
				return;
			}
			user.openPrivateChannel().complete().sendMessage("Você foi kickado por " + event.getAuthor().getAsMention() + ".\nMotivo: " + fReason).complete();
			BRjDevsBot.getDataManager().getData().cases.kick++;
			MemberListener.ignoreLeave(user.getId());
			event.getGuild().getController().kick(user.getId()).queue();
			ModLog.log(event.getMember(), event.getGuild().getMember(user), fReason, ModLog.ModAction.KICK);

			Utils.actionCascade(user.getId());
		});
		BRjDevsBot.getDataManager().update();
		event.reply("Feito! Kickei " + (String.join(", ", event.getMessage().getMentionedUsers().stream().map(Utils::getUser).collect(Collectors.toList()))) + " da guild.").queue();
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("kick");
	}

	@Override
	public String getDescription() {
		return "Kicka um usuário da Guild!";
	}

	@Override
	public String getExample() {
		return "kick <@268824727450025991> Exemplo";
	}

	@Override
	public boolean isAdminCommand() {
		return true;
	}
}
