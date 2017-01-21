package br.com.brjdevs.bot.utils;

import net.dv8tion.jda.core.entities.User;

public class Utils {
    public static boolean isEmpty(Object object) {
        return object == null || object.toString().isEmpty();
    }
    public static String getUser(User user) {
        return user.getName() + "#" + user.getDiscriminator();
    }
}
