package dev.mending.mines.gui;

import dev.mending.core.paper.api.gui.Gui;
import dev.mending.core.paper.api.gui.GuiIcon;
import dev.mending.core.paper.api.gui.pagination.PaginationManager;
import dev.mending.core.paper.api.item.ItemBuilder;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.mines.Mines;
import dev.mending.mines.data.Item;
import dev.mending.mines.mine.MineContent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class ContentGui extends Gui {

    private final Mines plugin;
    private final String name;
    private final PaginationManager pagination;
    private final Gui previousGui;


    public ContentGui(Mines plugin, String name, Gui previousGui) {

        super(Component.text("Edit content: " + name), 3);

        this.plugin = plugin;
        this.name = name;
        this.pagination = new PaginationManager(this);
        this.previousGui = previousGui;

        pagination.registerPageSlotsBetween(10, 16);

        fillRows(new GuiIcon(Item.PLACEHOLDER),1, 3);
        if (isEmpty()) {
            fillRows(new GuiIcon(Item.PLACEHOLDER),2);
        }
    }

    @Override
    public void update() {

        this.pagination.getItems().clear();

        if (this.previousGui != null) {
            setItem(isEmpty() ? 12 : 21, new GuiIcon(Item.BACK).onClick(clickEvent ->  {
                this.previousGui.open(player);
            }));
        }

        setItem(isEmpty() ? (this.previousGui != null ? 14 : 13): (this.previousGui != null ? 23 : 22), new GuiIcon(new ItemBuilder(Material.CRAFTER)
            .setName(Lang.deserialize("<yellow>Add Entry"))
        ).onClick(clickEvent -> {
            new EntryGui(plugin, name, -1, this).open(player);
        }));

        if (isEmpty()) {
            return;
        }

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

        for (int i = 0; i < plugin.getMineManager().getMines().get(name).getContent().size(); i++) {

            MineContent content = plugin.getMineManager().getMines().get(name).getContent().get(i);
            int finalI = i;

            pagination.addItem(new GuiIcon(new ItemBuilder(content.getBlockData().getMaterial())
                .setLore(Lang.deserialize("<gray>Chance: <yellow>" + content.getChance()))
            ).onClick(clickEvent -> {
                if (clickEvent.isLeftClick()) {
                    new EntryGui(plugin, name, finalI, this).open(player);
                } else {
                    new ConfirmGui(plugin, Component.text("Delete Entry?"), this, p -> {
                        plugin.getMineManager().getMines().get(name).getContent().remove(finalI);
                        plugin.getMineManager().save();
                    }).open(player);
                }
            }));
        }

        pagination.update();
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        save();
    }

    private void save() {
        plugin.getMineManager().save();
        player.sendMessage(plugin.getLanguage().get("contentSet").replaceText(Lang.replace("%name%", name)));
    }

    private boolean isEmpty() {
        return plugin.getMineManager().getMines().get(name).getContent().isEmpty();
    }

}
