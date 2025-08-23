package dev.mending.mines.mine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.mending.core.paper.api.config.json.Configuration;
import dev.mending.core.paper.api.config.json.adapter.ItemStackAdapter;
import dev.mending.core.paper.api.config.json.adapter.LocationAdapter;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


@Getter
public class MineManager extends Configuration {

    private final HashMap<String, Mine> mines;

    public MineManager(@NotNull JavaPlugin plugin) {
        super(plugin, "mines");
        this.mines = new HashMap<>();
    }

    @Override
    public Gson createGson() {
        return (new GsonBuilder())
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
            .registerTypeAdapter(Mine.class, new MineAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
    }

    @Override
    public void onLoad(JsonObject json) {

        this.mines.clear();

        for (Map.Entry<String, com.google.gson.JsonElement> entry : json.entrySet()) {
            String name = entry.getKey();
            Mine mine = gson.fromJson(entry.getValue(), Mine.class);
            mine.setName(name);
            this.mines.put(name, mine);
        }
    }

    @Override
    public void onPreSave(JsonObject json) {
        mines.forEach((name, mine) -> {
            for (Map.Entry<String, Mine> entry : mines.entrySet()) {
                json.add(entry.getKey(), gson.toJsonTree(entry.getValue(), Mine.class));
            }
        });
    }
}
