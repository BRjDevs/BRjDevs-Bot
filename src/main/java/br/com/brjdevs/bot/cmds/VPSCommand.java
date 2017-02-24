package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.core.EmbedBuilder;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;

public class VPSCommand implements ICommand {

	private OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	private double gb = 1024 * 1024 * 1024;

	@Override
	public void execute(CommandEvent event, String args) {
		double cpu = os.getSystemCpuLoad() * 100;
		double totalDiskSize = new File("/").getTotalSpace() / gb;
		double usableDiskSize = new File("/").getUsableSpace() / gb;
		double totalRam = os.getTotalPhysicalMemorySize() / gb;
		double freeRam = os.getFreePhysicalMemorySize() / gb;
		double usedRam = totalRam - freeRam;

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("VPS Stats");
		embedBuilder.addField("CPU Usage", String.format("%.2f", cpu) + "%", true);
		embedBuilder.addField("Disk size (USABLE/TOTAL)", String.format("%.1f", usableDiskSize) + "GB/" + String.format("%.1f", totalDiskSize) + "GB", false);
		embedBuilder.addField("RAM (TOTAL/FREE/USED)", String.format("%.2f", totalRam) + "GB/" + String.format("%.2f", freeRam) + "GB/" + String.format("%.2f", usedRam) + "GB", false);

		event.reply(embedBuilder.build()).queue();
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("vps");
	}

	@Override
	public String getDescription() {
		return "Mostra o stats da VPS.";
	}

	@Override
	public String getExample() {
		return null;
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}
}
