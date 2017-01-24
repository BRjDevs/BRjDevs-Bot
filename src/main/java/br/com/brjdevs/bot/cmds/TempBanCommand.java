package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.MemberListener;
import br.com.brjdevs.bot.core.ModLog;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.StringUtils;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.entities.User;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TempBanCommand implements ICommand {

	private static final Pattern pattern = Pattern.compile("\\d+?[a-zA-Z]");

	public static Iterable<String> iterate(Matcher matcher) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return new Iterator<String>() {
					@Override
					public boolean hasNext() {
						return matcher.find();
					}

					@Override
					public String next() {
						return matcher.group();
					}
				};
			}

			@Override
			public void forEach(Consumer<? super String> action) {
				while (matcher.find()) {
					action.accept(matcher.group());
				}
			}
		};
	}

	private static long parse(String s) {
		s = s.toLowerCase();
		long[] time = {0};
		iterate(pattern.matcher(s)).forEach(string -> {
			String l = string.substring(0, string.length() - 1);
			TimeUnit unit;
			switch (string.charAt(string.length() - 1)) {
				case 's':
					unit = TimeUnit.SECONDS;
					break;
				case 'm':
					unit = TimeUnit.MINUTES;
					break;
				case 'h':
					unit = TimeUnit.HOURS;
					break;
				case 'd':
					unit = TimeUnit.DAYS;
					break;
				default:
					unit = TimeUnit.SECONDS;
					break;
			}
			time[0] += unit.toMillis(Long.parseLong(l));
		});
		return time[0];
	}

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
		int index = reason.indexOf("time:");
		if (index < 0) {
			event.reply("Você não pode banir temporariamente um usuário sem me dizer o tempo!").queue();
			return;
		}
		String time = reason.substring(index);
		reason = reason.replace(time, "").trim();
		time = time.replaceAll("time:(\\s+)?", "");
		if (reason.isEmpty()) {
			event.reply("Você não pode banir temporariamente um usuário sem um motivo!").queue();
			return;
		}
		if (time.isEmpty()) {
			event.reply("Você não pode banir temporariamente um usuário sem me dizer o tempo!").queue();
			return;
		}
		long l = parse(time);
		String fReason = reason;
		String sTime = StringUtils.parseTime(l);
		event.getMessage().getMentionedUsers().forEach(user -> {
			if (user == event.getJDA().getSelfUser()) {
				event.reply("Você não pode me banir!").queue();
				return;
			}
			user.openPrivateChannel().complete().sendMessage("Você foi banido temporariamente por " + event.getAuthor().getAsMention() + ".\nMotivo: " + fReason + "\nTempo: " + sTime).complete();
			BRjDevsBot.getDataManager().getData().cases.tempban++;
			MemberListener.ignoreLeave(user.getId());
			event.getGuild().getController().ban(user, 7).queue();
			BRjDevsBot.getTempBanManager().addTempban(user.getId(), l + System.currentTimeMillis());
			ModLog.log(event.getMember(), event.getGuild().getMember(user), fReason, ModLog.ModAction.TEMP_BAN, sTime);

			Utils.actionCascade(user.getId());
		});
		BRjDevsBot.getDataManager().update();
		event.reply("The Ban Hammer has spoken! Banned " + (String.join(", ", event.getMessage().getMentionedUsers().stream().map(Utils::getUser).collect(Collectors.toList()))) + " por " + sTime + ".").queue();
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("tempban");
	}

	@Override
	public String getDescription() {
		return "Bane um usuário temporariamente.";
	}

	@Override
	public String getExample() {
		return "tempban <@268824727450025991> Exemplo time: 1d";
	}

	@Override
	public boolean isAdminCommand() {
		return true;
	}
}
