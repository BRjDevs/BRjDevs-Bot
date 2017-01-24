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
	private OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	@Override
	public void execute(CommandEvent event, String args) {
		double cpu = Math.floor(os.getSystemCpuLoad() * 10000) / 100;
		long totalDiskSize = new File("/").getTotalSpace();
		long usableDiskSize = new File("/").getUsableSpace();
		long totalRam = os.getTotalPhysicalMemorySize() / mb;
		long freeRam = os.getFreePhysicalMemorySize() / mb;

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("VPS Stats");
		embedBuilder.addField("CPU Usage", cpu + "%", true);
		embedBuilder.addField("Disk size (USABLE/TOTAL)", usableDiskSize + "/" + totalDiskSize, true);
		embedBuilder.addField("RAM (TOTAL/FREE/USING)", totalRam + "MB/" + freeRam + "MB/" + (totalRam - freeRam) + "MB", true);

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
