package dev.mending.mines.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.mines.Mines;
import dev.mending.mines.command.sub.*;
import dev.mending.mines.gui.MainGui;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class Command implements ICommand {

    private final Mines plugin;

    public Command(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("mines")
            .requires(sender -> sender.getSender().hasPermission("mines.command"))
            .executes(ctx -> {
                if (ctx.getSource().getSender() instanceof Player player) {
                    new MainGui(plugin).open(player);
                }
                return 1;
            })
            .then(new ReloadCommand(plugin).get())
            .then(new WandCommand(plugin).get())
            .then(new SaveCommand(plugin).get())
            .then(new DeleteCommand(plugin).get())
            .then(new ListCommand(plugin).get())
            .then(new ResetCommand(plugin).get())
            .then(new SetCommand(plugin).get())
            .build();
    }
}
