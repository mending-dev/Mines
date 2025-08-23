package dev.mending.mines.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.mines.Mines;
import dev.mending.mines.command.ICommand;
import dev.mending.mines.data.Item;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class WandCommand implements ICommand {

    private final Mines plugin;

    public WandCommand(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("wand")
            .requires(sender -> sender.getSender().hasPermission("mines.command.wand"))
            .executes(ctx -> {
                if (ctx.getSource().getSender() instanceof Player player) {
                    player.getInventory().addItem(Item.WAND);
                    player.sendMessage(plugin.getLanguage().get("wandReceived"));
                }
                return Command.SINGLE_SUCCESS;
            })
            .build();
    }
}
