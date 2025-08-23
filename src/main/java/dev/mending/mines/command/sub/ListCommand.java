package dev.mending.mines.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.mines.Mines;
import dev.mending.mines.command.ICommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class ListCommand implements ICommand {

    private final Mines plugin;

    public ListCommand(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("list")
            .requires(sender -> sender.getSender().hasPermission("mines.command.list"))
            .executes(ctx -> {

                // TODO: Custom Messages

                if (plugin.getMineManager().getMines().isEmpty()) {
                    ctx.getSource().getSender().sendRichMessage("No mines saved.");
                    return Command.SINGLE_SUCCESS;
                }

                ctx.getSource().getSender().sendRichMessage("List of all mines:");
                plugin.getMineManager().getMines().forEach((name, mine) -> {
                    ctx.getSource().getSender().sendRichMessage("- " + name + (mine.getDisplayName() != null ? " (" + mine.getDisplayName() + ")" : ""));
                });

                return Command.SINGLE_SUCCESS;
            })
            .build();
    }
}
