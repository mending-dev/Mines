package dev.mending.mines.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.mines.Mines;
import dev.mending.mines.command.ICommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class ReloadCommand implements ICommand {

    private final Mines plugin;

    public ReloadCommand(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("reload")
            .requires(sender -> sender.getSender().hasPermission("mines.command.reload"))
            .executes(ctx -> {
                plugin.getMineManager().reload();
                plugin.getLanguage().reload();
                ctx.getSource().getSender().sendRichMessage("<green>Configuration reloaded"); // TODO: Custom Message
                return Command.SINGLE_SUCCESS;
            })
            .build();
    }
}
