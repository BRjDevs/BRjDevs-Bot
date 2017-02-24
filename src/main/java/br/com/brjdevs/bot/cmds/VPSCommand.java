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

	private final int mb = 1024 * 1024;
	private OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();

	@Override
	public void execute(CommandEvent event, String args) {
		double cpu = os.getSystemCpuLoad() * 100;
		long totalDiskSize = new File("/").getTotalSpace() /mb;
		long usableDiskSize = new File("/").getUsableSpace() /mb;
		long totalRam = os.getTotalPhysicalMemorySize() / mb;
		long freeRam = os.getFreePhysicalMemorySize() / mb;
		long usedRam = totalRam - freeRam;

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("VPS Stats");
		embedBuilder.addField("CPU Usage", String.format("%.2f", cpu) + "%", true);
		embedBuilder.addField("Disk size (USABLE/TOTAL)", usableDiskSize + "MB/" + totalDiskSize + "MB", false);
		embedBuilder.addField("RAM (TOTAL/FREE/USED)", totalRam + "MB/" + freeRam + "MB/" + usedRam + "MB", false);

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
