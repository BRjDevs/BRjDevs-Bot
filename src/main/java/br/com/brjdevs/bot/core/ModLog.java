package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.utils.Utils;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

public class ModLog {

    public static void log(Member author, Member target, String reason, ModAction action, String... time) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField("Responsible Moderator", Utils.getUser(author.getUser()), true);
        JsonObject cases = BRjDevsBot.getDataManager().getData().get("cases").getAsJsonObject();
        embedBuilder.addField("Member", Utils.getUser(target.getUser()), true);
        embedBuilder.addField("Reason", reason, false);
        embedBuilder.setThumbnail(target.getUser().getEffectiveAvatarUrl());
        switch (action) {
            case BAN:
                embedBuilder.setAuthor("Ban | Case #" + cases.get("ban").getAsInt(), null, author.getUser().getEffectiveAvatarUrl());
                break;
            case TEMP_BAN:
                embedBuilder.setAuthor("Temp Ban | Case #" + cases.get("tempban").getAsInt(), null, author.getUser().getEffectiveAvatarUrl());
                embedBuilder.addField("Time", time[0], true);
                break;
            case KICK:
                embedBuilder.setAuthor("Kick | Case #" + cases.get("kick").getAsInt(), null, author.getUser().getEffectiveAvatarUrl());
                break;
        }
        BRjDevsBot.getLogChannel().sendMessage(embedBuilder.build()).queue();
    }

    public static void logunBan(Member author, String target, String reason) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField("Responsible Moderator", Utils.getUser(author.getUser()), true);
        embedBuilder.addField("Member ID", target, true);
        embedBuilder.addField("Reason", reason, false);
        embedBuilder.setAuthor("Unban", null, author.getUser().getEffectiveAvatarUrl());
        BRjDevsBot.getLogChannel().sendMessage(embedBuilder.build()).queue();
    }

    public enum ModAction {
        TEMP_BAN, BAN, KICK, UNBAN
    }
}
