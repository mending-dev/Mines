package dev.mending.mines.data;

import dev.mending.core.paper.api.item.ItemBuilder;
import dev.mending.core.paper.api.item.SkullBuilder;
import dev.mending.core.paper.api.language.Lang;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class Item {

    public static ItemStack WAND = new ItemBuilder(Material.WOODEN_PICKAXE)
        .setName(Lang.deserialize("<light_purple>Wand"))
        .setUnbreakable(true)
        .addItemFlags(ItemFlag.values())
        .build();

    public static ItemStack PLACEHOLDER = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
        .setName(Component.empty())
        .addItemFlags(ItemFlag.values())
        .build();

    public static ItemStack PREVIOUS = new SkullBuilder("MHF_ArrowLeft")
        .setName(Lang.deserialize("<gray>Previous"))
        .addItemFlags(ItemFlag.values())
        .build();

    public static ItemStack NEXT = new SkullBuilder("MHF_ArrowRight")
        .setName(Lang.deserialize("<gray>Next"))
        .addItemFlags(ItemFlag.values())
        .build();

    public static ItemStack DECREASE = new SkullBuilder("MHF_ArrowDown")
        .setName(Lang.deserialize("<red><bold>-"))
        .build();

    public static ItemStack INCREASE = new SkullBuilder("MHF_ArrowUp")
        .setName(Lang.deserialize("<yellow><bold>+"))
        .build();

    public static ItemStack BACK = new ItemBuilder(Material.OAK_DOOR)
        .setName(Lang.deserialize("<red>Back to Main Menu"))
        .addItemFlags(ItemFlag.values())
        .build();

}
