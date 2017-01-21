package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class MemberListener implements EventListener {
    @Override
    public void onEvent(Event e) {
        if (e instanceof GuildMemberLeaveEvent) {
            GuildMemberLeaveEvent event = (GuildMemberLeaveEvent) e;
            send(event.getMember(), true);
        } else if (e instanceof GuildMemberJoinEvent) {
            GuildMemberJoinEvent event = (GuildMemberJoinEvent) e;
            send(event.getMember(), false);
        }
    }
    public static void send(Member member, boolean leave) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(Utils.getUser(member.getUser()) + (leave ? " has left the guild!" : " has joined the guild!"), null, BRjDevsBot.getGuild().getIconUrl());
        builder.setDescription(
                "**User ID:** " + member.getUser().getId() + "");
        builder.setFooter("Total Members: " + BRjDevsBot.getGuild().getMembers().size(), null);
        builder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
        BRjDevsBot.getLogChannel().sendMessage(builder.build()).queue();
    }
}
