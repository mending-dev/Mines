package dev.mending.mines.mine;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

@Getter @Setter
public class MineContent {

    private BlockData blockData;
    private float chance;

    public MineContent(String blockType, float chance) {
        Material material = Material.getMaterial(blockType);
        if (material == null) { material = Material.BEDROCK; }
        this.blockData = material.createBlockData();
        this.chance = chance;
    }
}
