package br.com.brjdevs.bot.utils;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.MemberListener;
import br.com.brjdevs.bot.core.ModLog;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {
	//In case of any Kick/Ban/Tempban of Owner, Bots are also kicked.
	public static void actionCascade(String userid) {
		Set<Member> members = BRjDevsBot.getDataManager().getData().bots.entrySet()
			.stream().filter(entry -> entry.getValue().owners.contains(userid))
			.map(Entry::getKey)
			.map(BRjDevsBot.getGuild()::getMemberById)
			.collect(Collectors.toSet());

		members.forEach(member -> {
			MemberListener.ignoreLeave(member.getUser().getId());
			member.getGuild().getController().kick(member).queue();
			ModLog.log(BRjDevsBot.getGuild().getSelfMember(), member, "O dono do Bot foi kickado da Guild", ModLog.ModAction.KICK);
		});
	}

	public static String getUser(User user) {
		return user.getName() + "#" + user.getDiscriminator();
	}

	public static boolean isEmpty(Object object) {
		return object == null || object.toString().isEmpty();
	}
}
