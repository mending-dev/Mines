package dev.mending.mines.selection;

import org.bukkit.Location;

public record Selection(Location pos1, Location pos2) {

    public boolean isValid() {
        return this.pos1 != null && this.pos2 != null;
    }
}
