package br.com.brjdevs.bot.core.commands;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandManager {
	private static final SimpleLog LOG = SimpleLog.getLog("CommandManager");
	private List<ICommand> commands;
	private HelpContainer helpContainer;

	public CommandManager() {
		this.commands = new ArrayList<>();
		this.helpContainer = new HelpContainer();
	}

	public ICommand get(String alias) {
		return commands.stream().filter(cmd -> cmd.getAliases().contains(alias)).findFirst().orElse(null);
	}

	public List<ICommand> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	public HelpContainer getHelpContainer() {
		return helpContainer;
	}

	public void load() {
		Reflections reflections = new Reflections("br.com.brjdevs.bot.cmds");
		Set<Class<? extends ICommand>> commands = reflections.getSubTypesOf(ICommand.class);
		commands.forEach(clazz -> {
			try {
				register(clazz.newInstance());
			} catch (Exception e) {
				LOG.fatal("Failed to register Command " + clazz.getSimpleName());
				LOG.log(e);
			}
		});
	}

	private void register(ICommand command) {
		helpContainer.createHelp(command);
		this.commands.add(command);
	}
}
