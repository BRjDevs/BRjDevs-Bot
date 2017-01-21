package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.StringUtils;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.core.EmbedBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class VPSCommand implements ICommand {

    private OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private final int mb = 1024 * 1024;

    @Override
    public void execute(CommandEvent event, String args) {
        double cpu = Math.floor(os.getSystemCpuLoad()  * 10000) / 100;
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
    public boolean isAdminCommand() {
        return false;
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
    public List<String> getAliases() {
        return Collections.singletonList("vps");
    }
}
