package br.com.brjdevs.bot.cmds;

import br.com.brjdevs.bot.BRjDevsBot;
import br.com.brjdevs.bot.core.commands.CommandEvent;
import br.com.brjdevs.bot.core.commands.ICommand;
import br.com.brjdevs.bot.utils.Utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Collections;
import java.util.List;

public class EvalCommand implements ICommand {
	private final ScriptEngine scriptEngine;

	public EvalCommand() {
		this.scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
		this.scriptEngine.put("jda", BRjDevsBot.getJDA());
		this.scriptEngine.put("bot", BRjDevsBot.getJDA().getSelfUser());
		this.scriptEngine.put("guild", BRjDevsBot.getGuild());
	}

	@Override
	public void execute(CommandEvent event, String args) {
		scriptEngine.put("channel", event.getChannel());
		scriptEngine.put("msg", event.getMessage());
		scriptEngine.put("author", event.getAuthor());
		scriptEngine.put("member", event.getMember());
		scriptEngine.put("event", event);
		scriptEngine.put("args", args);

		Object o;
		try {
			scriptEngine.eval("imports = new JavaImporter(java.util, java.io, java.net)\n");
			o = scriptEngine.eval("(function() {with(imports) {" + args + "\n}})()");
		} catch (Exception e) {
			o = e;
		}

		if (Utils.isEmpty(o)) {
			o = "Evaluated successfully and no objects returned.";
		}

		event.reply(o.toString()).queue();
	}

	@Override
	public List<String> getAliases() {
		return Collections.singletonList("eval");
	}

	@Override
	public String getDescription() {
		return "Executa códigos usando JS!";
	}

	@Override
	public String getExample() {
		return "eval return \"this is JS\"";
	}

	@Override
	public boolean isAdminCommand() {
		return true;
	}
}
