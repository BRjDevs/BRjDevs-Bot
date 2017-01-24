package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.MemberListener;
import br.com.brjdevs.bot.core.ModLog;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.entities.User;

import java.util.Arrays;
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
		String fReason = reason;
		event.getMessage().getMentionedUsers().forEach(user -> {
			if (user == event.getJDA().getSelfUser()) {
				event.reply("Você não pode me banir!").queue();
				return;
			}
			user.openPrivateChannel().complete().sendMessage("Você foi banido por " + event.getAuthor().getAsMention() + ".\nMotivo: " + fReason).complete();
			BRjDevsBot.getDataManager().getData().cases.ban++;
			MemberListener.ignoreLeave(user.getId());
			event.getGuild().getController().ban(user, 7).queue();
			ModLog.log(event.getMember(), event.getGuild().getMember(user), fReason, ModLog.ModAction.BAN);

			Utils.actionCascade(user.getId());
		});
		BRjDevsBot.getDataManager().update();
		event.reply("O chão treme com o poder do BanHammer! Bani " + (event.getMessage().getMentionedUsers().stream().map(Utils::getUser).collect(Collectors.joining(", "))) + " da Guild.").queue();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("ban", "banhammer", "getout", "vaza");
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
	public boolean isAdminCommand() {
		return true;
	}
}
