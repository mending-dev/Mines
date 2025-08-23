package dev.mending.mines.gui;

import dev.mending.core.paper.api.gui.Gui;
import dev.mending.core.paper.api.gui.GuiIcon;
import dev.mending.core.paper.api.gui.pagination.PaginationManager;
import dev.mending.core.paper.api.item.ItemBuilder;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.mines.Mines;
import dev.mending.mines.data.Item;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MainGui extends Gui {

    private final Mines plugin;
    private final PaginationManager pagination;

    public MainGui(Mines plugin) {

        super(Component.text("Mines"), 3);

        this.plugin = plugin;
        this.pagination = new PaginationManager(this);

        pagination.registerPageSlotsBetween(10, 16);

        fillRows(new GuiIcon(Item.PLACEHOLDER),1, 3);
        if (isEmpty()) {
            fillRows(new GuiIcon(Item.PLACEHOLDER),2);
        }
    }

    @Override
    public void update() {

        if (isEmpty()) {

            setItem(13, new GuiIcon(new ItemBuilder(Material.CRAFTER)
                .setName(Lang.deserialize("<yellow>Create your first mine"))
            ).onClick(clickEvent -> {
                player.getInventory().addItem(Item.WAND);
                player.sendMessage(Lang.deserialize("<gray>Use the wand to set positions. Save your mine with <yellow>/mines save [name]"));
                player.closeInventory();
            }));

            return;
        }

        pagination.getItems().clear();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            ItemStack previousItem = Item.PREVIOUS;
            ItemStack nextItem = Item.NEXT;

            Bukkit.getScheduler().runTask(plugin, () -> {

                setItem(9, new GuiIcon(previousItem).onClick(clickEvent -> {
                    this.pagination.goPreviousPage();
                    this.pagination.update();
                }));

                setItem(17, new GuiIcon(nextItem).onClick(clickEvent -> {
                    this.pagination.goNextPage();
                    this.pagination.update();
                }));
            });
        });

        renderMines();
        pagination.update();
    }

    private void renderMines() {
        plugin.getMineManager().getMines().forEach((name, mine) -> {
            pagination.addItem(new GuiIcon(new ItemBuilder(Material.WOODEN_PICKAXE)
                    .setName(Lang.deserialize(name))
                    .addItemFlags(ItemFlag.values())
            ).onClick(clickEvent -> {
                if (clickEvent.isLeftClick()) {
                    new ContentGui(plugin, name, this).open(player);
                } else {
                    new ConfirmGui(plugin, Component.text("Delete Mine?"), this, p -> {
                        plugin.getMineManager().getMines().remove(name);
                        plugin.getMineManager().save();
                    }).open(player);
                }
            }));
        });
    }

    private boolean isEmpty() {
        return plugin.getMineManager().getMines().isEmpty();
    }
}
