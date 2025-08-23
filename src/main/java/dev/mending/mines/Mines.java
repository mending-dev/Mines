package dev.mending.mines;

import dev.mending.core.paper.api.language.json.Language;
import dev.mending.mines.command.Command;
import dev.mending.mines.expansion.PAPIExpansion;
import dev.mending.mines.listener.InteractionListener;
import dev.mending.mines.task.ResetTask;
import dev.mending.mines.selection.SelectionCache;
import dev.mending.mines.mine.MineManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Mines extends JavaPlugin {

    private final Language language = new Language(this);
    private final MineManager mineManager = new MineManager(this);

    private final SelectionCache selectionCache = new SelectionCache();

    private ResetTask resetTask;

    @Override
    public void onEnable() {

        this.language.init();
        this.mineManager.init();

        this.resetTask = new ResetTask(this);
        this.resetTask.runTaskTimer(this, 20L, 20L);

        registerEvents(getServer().getPluginManager());
        registerCommands();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPIExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        this.selectionCache.clear();
    }

    private void registerEvents(final PluginManager pluginManager) {
        pluginManager.registerEvents(new InteractionListener(this), this);
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new Command(this).get());
        });
    }
}
