package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.core.data.objects.Data.Bot;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.util.*;
import java.util.stream.Collectors;

public class BotsCommand implements ICommand {
	@Override
	public void execute(CommandEvent event, String args) {
		if (args.isEmpty()) {
			event.reply(addBots(
				new EmbedBuilder()
					.setTitle("Bot List")
					.setColor(event.getGuild().getSelfMember().getColor()),
				BRjDevsBot.getGuild().getMembers()
			).build()).queue();
		} else if (!event.getMessage().getMentionedUsers().isEmpty()) {
			User user = event.getMessage().getMentionedUsers().get(0);
			if (user.isBot()) {
				event.reply(botEmbed(BRjDevsBot.getGuild().getMember(user))).queue();
			} else {
				List<Member> ownedBots = BRjDevsBot.getDataManager().getData().bots.entrySet().stream()
					.filter(entry -> entry.getValue().owners.contains(user.getId()))
					.map(Map.Entry::getKey)
					.map(event.getGuild()::getMemberById)
					.collect(Collectors.toList());

				if (ownedBots.size() == 0) {
					event.reply("O Usuário Mencionado não é dono de nennhum Bot Registrado.").queue();
					return;
				}

				event.reply(addBots(
					new EmbedBuilder()
						.setTitle("Bots Owned by " + Utils.getUser(user))
						.setThumbnail(user.getEffectiveAvatarUrl())
						.setColor(event.getGuild().getSelfMember().getColor()),
					ownedBots
				).build()).queue();
			}
		} else {
			event.reply("Você não mencionou nenhum bot. Tente executar o comando sem argumentos ou mencionando um bot.").queue();
		}
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("bots", "botes", "robots", "robos", "escravos");
	}

	@Override
	public String getDescription() {
		return "Lista os Bots da Guild.";
	}

	@Override
	public String getExample() {
		return null;
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}

	private EmbedBuilder addBots(EmbedBuilder builder, List<Member> members) {
		members.stream()
			.filter(Objects::nonNull)
			.filter(member -> member.getUser().isBot())
			.sorted(Comparator.comparing(m -> Utils.getUser(m.getUser())))
			.forEach(member ->
				builder.addField(Utils.getUser(member.getUser()), botDescription(member), true));
		return builder;
	}

	private String botDescription(Member member) {
		if (!member.getUser().isBot()) throw new IllegalStateException("Member is not a Bot ._.");
		Bot botInfo = BRjDevsBot.getDataManager().getData().bots.get(member.getUser().getId());
		if (botInfo == null) return "<Bot não está Registrado>";
		return String.format(
			"\n**Descrição**: \n%s\n**Prefixo**: ``%s``\n**Dono(s)**: %s\n**Bot de Música**: %s\n**Bot de Desenvolvimento/Testes**: %s",
			botInfo.description.isEmpty() ? "<Sem Descrição>" : botInfo.description,
			botInfo.prefix.isEmpty() ? "<Sem Prefixo Definido>" : botInfo.prefix,
			botInfo.owners.stream()
				.map(BRjDevsBot.getGuild()::getMemberById)
				.filter(Objects::nonNull)
				.map(Member::getEffectiveName)
				.collect(Collectors.joining(", ")), botInfo.hasMusic ? "✅" : "❌", botInfo.isDevBot ? "✅" : "❌"
		);
	}

	private MessageEmbed botEmbed(Member member) {
		if (!member.getUser().isBot()) throw new IllegalStateException("Member is not a Bot ._.");
		Bot botInfo = BRjDevsBot.getDataManager().getData().bots.get(member.getUser().getId());
		if (botInfo == null) return new EmbedBuilder()
			.setTitle(member.getEffectiveName())
			.setImage(member.getUser().getEffectiveAvatarUrl())
			.setColor(member.getColor())
			.addField("Sem Informações", "<Bot não está Registrado>", false)
			.build();

		return new EmbedBuilder()
			.setTitle(member.getEffectiveName())
			.setThumbnail(member.getUser().getEffectiveAvatarUrl())
			.setColor(member.getColor())
			.addField("Descrição:", botInfo.description.isEmpty() ? "<Sem Descrição>" : botInfo.description, false)
			.addField("Prefixo:", "``" + (botInfo.prefix.isEmpty() ? "<Sem Prefixo Definido>" : botInfo.prefix) + "``", false)
			.addField("Dono(s):", botInfo.owners.stream().map(s -> BRjDevsBot.getGuild().getMemberById(s)).filter(Objects::nonNull).map(Member::getEffectiveName).collect(Collectors.joining(", ")), false)
			.addField("Bot de Música:", botInfo.hasMusic ? "✅" : "❌", false)
			.addField("Bot de Desenvolvimento/Testes:", botInfo.isDevBot ? "✅" : "❌", false)
			.build();
	}
}
