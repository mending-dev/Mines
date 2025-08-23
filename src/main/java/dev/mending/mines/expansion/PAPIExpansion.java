package dev.mending.mines.expansion;

import dev.mending.mines.Mines;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

    private final Mines plugin;

    public PAPIExpansion(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "mines";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        String[] parts = params.split("_");
        if (parts.length < 2) {
            return null;
        }

        String mineName = parts[0];
        var mine = plugin.getMineManager().getMines().get(mineName);
        if (mine == null) {
            return null;
        }

        if (parts.length == 2 && parts[1].equalsIgnoreCase("timer")) {
            long remaining = mine.getNextResetTime() - System.currentTimeMillis();
            if (remaining < 0) remaining = 0;
            long seconds = remaining / 1000;
            long minutes = seconds / 60;
            long sec = seconds % 60;
            return minutes + "m " + sec + "s";
        }

        if (parts.length == 2 && parts[1].equalsIgnoreCase("name")) {
            return mine.getDisplayName() != null ? mine.getDisplayName() : mineName;
        }

        return null;
    }
}