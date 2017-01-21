package br.com.brjdevs.bot.core;

import br.com.brjdevs.bot.BRjDevsBot;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TempBanManager {
    private final Map<String, Long> UNBANS;
    private boolean unbansUpdated = false;
    public TempBanManager() {
        this(new HashMap<>());
    }

    public TempBanManager(Map<String,Long> unbans) {
        UNBANS = Collections.synchronizedMap(unbans);

        Thread thread = new Thread(this::threadcode, "Tempbans Thread");
        thread.setDaemon(true);
        thread.start();
    }

    public void addTempban(String id, Long milis) {
        BRjDevsBot.getDataManager().getData().get("tempbans").getAsJsonObject().addProperty(id, milis);
        UNBANS.put(id, milis);
        unbansUpdated = true;
        synchronized (this) {
            notify();
        }
    }

    public void removeTempban(String id) {
        if (UNBANS.containsKey(id)) {
            UNBANS.remove(id);
            unbansUpdated = true;
            synchronized (this) {
                notify();
            }
        }
    }

    private void threadcode() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (UNBANS.isEmpty()) {
                try {
                    synchronized (this) {
                        wait();
                        unbansUpdated = false;
                    }
                } catch (InterruptedException ignored) {
                }
            }

            //noinspection OptionalGetWithoutIsPresent
            Entry<String, Long> unbanFirstEntry = UNBANS.entrySet().stream().sorted(Comparator.comparingLong(Entry::getValue)).findFirst().get();

            try {
                long timeout = unbanFirstEntry.getValue() - System.currentTimeMillis();
                if (timeout > 0) {
                    synchronized (this) {
                        wait(timeout);
                    }
                }
            } catch (InterruptedException ignored) {
            }

            if (!unbansUpdated) {
                UNBANS.remove(unbanFirstEntry.getKey());
                BRjDevsBot.getGuild().getController().unban(unbanFirstEntry.getKey());
                BRjDevsBot.getDataManager().getData().get("tempbans").getAsJsonObject().remove(unbanFirstEntry.getKey());
                ModLog.logunBan(BRjDevsBot.getGuild().getSelfMember(), unbanFirstEntry.getKey(), "The temporary ban ended.");
                BRjDevsBot.getDataManager().update();
            } else unbansUpdated = false; //and the loop will restart and resolve it
        }
    }
}