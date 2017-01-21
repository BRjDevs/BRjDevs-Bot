package br.com.brjdevs.bot.utils;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class ChannelUtils {

    public static void show(TextChannel channel, Member member) {
        PermissionOverride permOverr = channel.getPermissionOverride(member);
        PermissionOverride everyoneOverr = channel.getPermissionOverride(channel.getGuild().getPublicRole());
        if (everyoneOverr.getAllowed().contains(Permission.MESSAGE_READ)) {
            permOverr.delete().queue();
            return;
        }
        if (permOverr == null) {
            channel.createPermissionOverride(member).queue(permissionOverride ->
                    permissionOverride.getManager().grant(Permission.MESSAGE_READ).queue());
        } else {
            permOverr.getManager().grant(Permission.MESSAGE_READ).queue();
        }
    }
    public static void hide(TextChannel channel, Member member) {
        PermissionOverride permOverr = channel.getPermissionOverride(member);
        PermissionOverride everyoneOverr = channel.getPermissionOverride(channel.getGuild().getPublicRole());
        if (everyoneOverr.getDenied().contains(Permission.MESSAGE_READ)) {
            permOverr.delete().queue();
            return;
        }
        if (permOverr == null) {
            channel.createPermissionOverride(member).queue(permissionOverride ->
                    permissionOverride.getManager().deny(Permission.MESSAGE_READ).queue());
        } else {
            permOverr.getManager().deny(Permission.MESSAGE_READ).queue();
        }
    }
}
