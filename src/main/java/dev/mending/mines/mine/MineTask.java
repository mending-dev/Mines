package dev.mending.mines.mine;

import dev.mending.mines.Mines;
import org.bukkit.scheduler.BukkitRunnable;

public class MineTask extends BukkitRunnable {

    private final Mines plugin;

    public MineTask(Mines plugin) {
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
