package dev.mending.mines.gui;

import dev.mending.core.paper.api.gui.Gui;
import dev.mending.core.paper.api.gui.GuiIcon;
import dev.mending.core.paper.api.item.ItemBuilder;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.mines.Mines;
import dev.mending.mines.data.Item;
import dev.mending.mines.mine.MineContent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EntryGui extends Gui {

    private final Mines plugin;
    private final String name;
    private final int index;
    private final Gui previousGui;

    private MineContent content;
    private MineContent initialContent;

    public EntryGui(Mines plugin, String name, int index, Gui previousGui) {

        super(Component.text(index == -1 ? "Add Entry" : "Edit Entry"), 6);

        this.plugin = plugin;
        this.name = name;
        this.index = index;
        this.previousGui = previousGui;

        ItemBuilder itemBuilder;

        if (index != -1) {
            content = plugin.getMineManager().getMines().get(name).getContent().get(index);
            initialContent = content;
            itemBuilder = new ItemBuilder(initialContent.getBlockData().getMaterial());
        } else {
            itemBuilder = new ItemBuilder(Material.BARRIER).setName(Lang.deserialize("<red>Drag your block here"));
        }

        fill(new GuiIcon(Item.PLACEHOLDER));

        getActionSlotManager().register(13, itemBuilder)
            .onPrePutClick(((clickEvent, itemStack) -> !itemStack.getType().isBlock()))
            .onPrePickupClick((clickEvent, itemStack) -> index == -1 ? itemStack.getType() == Material.BARRIER : itemStack.getType().equals(initialContent.getBlockData().getMaterial()))
            .onPut((clickEvent, itemStack) -> {
                this.content = new MineContent(itemStack.getType().toString(), 100);
                update();
            })
            .onPickup((clickEvent, itemStack) -> {
                this.content = index == -1 ? null : initialContent;
                update();
            });
    }

    @Override
    public void update() {

        if (this.content == null) {

            setItem(21, new GuiIcon(Item.PLACEHOLDER));
            setItem(22, new GuiIcon(Item.PLACEHOLDER));
            setItem(23, new GuiIcon(Item.PLACEHOLDER));

            setItem(40, new GuiIcon(Item.BACK).onClick(e -> {
                this.previousGui.open(player);
            }));

        } else {

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

                ItemStack decreaseItem = Item.DECREASE;
                ItemStack increaseItem = Item.INCREASE;

                Bukkit.getScheduler().runTask(plugin, () -> {

                    setItem(21, new GuiIcon(decreaseItem).onClick(e -> {
                        if (e.isRightClick()) {
                            if (content.getChance() > 1) {
                                content.setChance(content.getChance() - 1);
                            }
                        } else {
                            if (content.getChance() > 10) {
                                content.setChance(content.getChance() - 10);
                            }
                        }
                        update();
                    }));

                    setItem(23, new GuiIcon(increaseItem).onClick(e -> {
                        if (e.isRightClick()) {
                            if (content.getChance() < 100) {
                                content.setChance(content.getChance() + 1);
                            }
                        } else {
                            if (content.getChance() < 91) {
                                content.setChance(content.getChance() + 10);
                            }
                        }
                        update();
                    }));
                });
            });

            setItem(22, new GuiIcon(new ItemBuilder(Material.PAPER)
                    .setName(Lang.deserialize("<gray>Chance: <yellow>" + content.getChance()))
            ));

            setItem(40, new GuiIcon(new ItemBuilder(Material.BOOK).setName(Lang.deserialize("<yellow>Save"))).onClick(e -> {
                save();
                this.previousGui.open(player);
            }));
        }
    }

    private void save() {
        if (index == -1) {
            plugin.getMineManager().getMines().get(name).getContent().add(content);
        } else {
            plugin.getMineManager().getMines().get(name).getContent().set(index, content);
        }
        plugin.getMineManager().save();
    }
}
