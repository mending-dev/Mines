package dev.mending.mines.gui;

import dev.mending.core.paper.api.gui.Gui;
import dev.mending.core.paper.api.gui.GuiIcon;
import dev.mending.core.paper.api.item.ItemBuilder;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.mines.Mines;
import dev.mending.mines.data.Item;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ConfirmGui extends Gui {

    private final Mines plugin;
    private final Gui previousGui;
    private final Consumer<Player> action;

    public ConfirmGui(Mines plugin, @NotNull Component title, Gui previousGui, Consumer<Player> action) {
        super(title, 3);
        this.plugin = plugin;
        this.previousGui = previousGui;
        this.action = action;
    }

    @Override
    public void update() {

        fill(new GuiIcon(Item.PLACEHOLDER));

        setItem(12, new GuiIcon(new ItemBuilder(Material.GRAY_DYE)
            .setName(Lang.deserialize("<red>Cancel"))
        ).onClick(clickEvent -> {
            this.previousGui.open(player);
        }));

        setItem(14, new GuiIcon(new ItemBuilder(Material.LIME_DYE)
                .setName(Lang.deserialize("<yellow>Confirm"))
        ).onClick(clickEvent -> {
            action.accept(player);
            this.previousGui.open(player);
        }));
    }
}
