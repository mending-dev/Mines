package dev.mending.mines.mine;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Getter @Setter
public class Mine extends MineCuboid {

    private String name;
    private String displayName;
    private List<MineContent> content;

    public Mine(String name, Location pos1, Location pos2) {
        super(pos1, pos2);
        this.name = name;
        this.content = new ArrayList<>();
    }

    public void reset() {

        if (content == null || content.isEmpty()) return;

        double totalChance = 0;

        for (MineContent content : content) {
            totalChance += content.getChance();
        }

        Random random = new Random();

        Iterator<Block> blockIterator = blockList();
        while (blockIterator.hasNext()) {

            double r = random.nextDouble() * totalChance;

            double cumulative = 0;
            for (MineContent content : content) {
                cumulative += content.getChance();
                if (r <= cumulative) {
                    blockIterator.next().setBlockData(content.getBlockData(), false);
                    break;
                }
            }

        }
    }
}