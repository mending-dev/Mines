package dev.mending.mines.data;

import dev.mending.core.paper.api.item.ItemBuilder;
import dev.mending.core.paper.api.language.Lang;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class Item {

    public static ItemStack WAND = new ItemBuilder(Material.WOODEN_PICKAXE)
        .setName(Lang.deserialize("<light_purple>Wand"))
        .setUnbreakable(true)
        .addItemFlags(ItemFlag.values())
        .build();

}
