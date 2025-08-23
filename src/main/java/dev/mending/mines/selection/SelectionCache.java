package dev.mending.mines.selection;

import org.bukkit.Location;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SelectionCache {

    private final ConcurrentHashMap<UUID, Location> pos1 = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Location> pos2 = new ConcurrentHashMap<>();

    public void setPos1(UUID uuid, Location location) {
        this.pos1.put(uuid, location);
    }

    public void setPos2(UUID uuid, Location location) {
        this.pos2.put(uuid, location);
    }

    public void clear(UUID uuid) {
        this.pos1.remove(uuid);
        this.pos2.remove(uuid);
    }

    public void clear() {
        this.pos1.clear();
        this.pos2.clear();
    }

    public Selection get(UUID uuid) {
        return new Selection(pos1.get(uuid), pos2.get(uuid));
    }

}
