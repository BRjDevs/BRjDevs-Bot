package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.core.data.objects.Data.Bot;
import net.dv8tion.jda.core.entities.Member;

import java.util.Arrays;
import java.util.List;

public class SetBotsInfoCommand implements ICommand {
	@Override
	public void execute(CommandEvent event, String args) {
		String[] values = args.split("\\s+?", 3);

		String botId = values[0];
		if (botId.startsWith("<@")) botId = botId.substring(2, values[0].length() - 1);
		if (botId.startsWith("!")) botId = botId.substring(1);

		Member member = event.getGuild().getMemberById(botId);
		if (member == null || !member.getUser().isBot()) {
			event.reply("Member must be valid and a Bot.").queue();
			return;
		}

		if (!BRjDevsBot.getAdmins().contains(event.getMember()) && !BRjDevsBot.getDataManager().isBotOwner(member, event.getMember())) {
			event.reply("You must be the bot owner to use the command. Please call an Admin to set you as a the Bot Owner").queue();
			return;
		}

		Bot botInfo = BRjDevsBot.getDataManager().getData().bots.computeIfAbsent(botId, s -> new Bot());

		if (values.length < 3 || values[1] == null || values[1].isEmpty() || values[2] == null || values[2].isEmpty()) {
			event.reply("You must supply an parameter and a value to set").queue();
			return;
		}

		String param = values[1], value = values[2];

		if (param.equals("addowner") || param.equals("setowner")) {
			if (value.startsWith("<@")) value = value.substring(2, value.length() - 1);
			if (value.startsWith("!")) value = value.substring(1);

			Member newOwner = event.getGuild().getMemberById(value);

			if (newOwner == null || newOwner.getUser().isBot()) {
				event.reply("New Owner must be valid and not a Bot.").queue();
				return;
			}

			if (param.equals("setowner")) botInfo.owners.clear();

			if (botInfo.owners.add(value)) {
				event.reply("New Owner added Successfully.").queue();
			} else {
				event.reply("New Owner was already added.").queue();
			}
			BRjDevsBot.getDataManager().update();
			return;
		}

		if (param.equals("rmowner") || param.equals("removeowner")) {
			if (value.startsWith("<@")) value = value.substring(2, value.length() - 1);
			if (value.startsWith("!")) value = value.substring(1);

			Member newOwner = event.getGuild().getMemberById(value);

			if (newOwner == null || newOwner.getUser().isBot()) {
				event.reply("Owner must be valid and not a Bot.").queue();
				return;
			}

			if (botInfo.owners.remove(value)) {
				event.reply("Owner removed Successfully.").queue();
			} else {
				event.reply("Owner wasn't one of the Bots Owner.").queue();
			}
			BRjDevsBot.getDataManager().update();
			return;
		}

		if (param.equals("setmusicbot") || param.equals("setmusic")) {
			botInfo.hasMusic = value.startsWith("t") || value.startsWith("s") || value.startsWith("y");
			event.reply("Value set.").queue();
			BRjDevsBot.getDataManager().update();
			return;
		}

		if (param.equals("setdevbot") || param.equals("setdev") || param.equals("settestbot") || param.equals("settest")) {
			botInfo.isDevBot = value.startsWith("t") || value.startsWith("s") || value.startsWith("y");
			event.reply("Value set.").queue();
			BRjDevsBot.getDataManager().update();
			return;
		}

		if (param.equals("setprefix") || param.equals("setbotprefix")) {
			botInfo.prefix = value;
			event.reply("Value set.").queue();
			BRjDevsBot.getDataManager().update();
			return;
		}

		if (param.equals("setdesc") || param.equals("setbotdesc") || param.equals("setdescription") || param.equals("setbotdescription")) {
			botInfo.description = value;
			event.reply("Value set.").queue();
			BRjDevsBot.getDataManager().update();
			return;
		}

		//TODO CONTINUE
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("setbotinfo", "setbotsinfo");
	}

	@Override
	public String getDescription() {
		return "Define informações sobre os Bots da Guild.";
	}

	@Override
	public String getExample() {
		return "setbotinfo <addowner|setowner|rmowner|setmusic|setdev|setprefix|setdesc> <value>";
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}
}
