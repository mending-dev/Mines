package dev.mending.mines.task;

import dev.mending.mines.Mines;
import dev.mending.mines.mine.Mine;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetTask extends BukkitRunnable {

    private final Mines plugin;

    public ResetTask(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        long now = System.currentTimeMillis();

        for (Mine mine : plugin.getMineManager().getMines().values()) {
            if (now >= mine.getNextResetTime()) {
                mine.reset();
                mine.setNextResetTime(now + mine.getResetInterval() * 1000);
            }
        }
    }
}
