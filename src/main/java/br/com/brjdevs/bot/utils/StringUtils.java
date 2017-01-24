package br.com.brjdevs.bot.utils;

import java.util.Arrays;

public class StringUtils {
	public static String[] normalizeArray(String[] raw, int expectedSize) {
		String[] normalized = new String[expectedSize];

		Arrays.fill(normalized, "");
		for (int i = 0; i < normalized.length; i++) {
			if (i < raw.length && raw[i] != null && !raw[i].isEmpty()) {
				normalized[i] = raw[i];
			}
		}
		return normalized;
	}

	public static String parseTime(long duration) {
		final long
			years = duration / 31104000000L,
			months = duration / 2592000000L % 12,
			days = duration / 86400000L % 30,
			hours = duration / 3600000L % 24,
			minutes = duration / 60000L % 60,
			seconds = duration / 1000L % 60;
		String uptime = (years == 0 ? "" : years + " Years, ") + (months == 0 ? "" : months + " Months, ")
			+ (days == 0 ? "" : days + " Days, ") + (hours == 0 ? "" : hours + " Hours, ")
			+ (minutes == 0 ? "" : minutes + " Minutes, ") + (seconds == 0 ? "" : seconds + " Seconds, ");

		uptime = StringUtils.replaceLast(uptime, ", ", "");
		return StringUtils.replaceLast(uptime, ",", " and");
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}

	public static String[] splitArgs(String args, int expectedArgs) {
		String[] raw = args.split("\\s+", expectedArgs);
		if (expectedArgs < 1) return raw;
		return normalizeArray(raw, expectedArgs);
	}

}
