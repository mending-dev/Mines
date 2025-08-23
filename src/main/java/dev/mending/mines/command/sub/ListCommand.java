package dev.mending.mines.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
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

                if (plugin.getMineManager().getMines().isEmpty()) {
                    ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("listEmpty"));
                    return Command.SINGLE_SUCCESS;
                }

                ctx.getSource().getSender().sendMessage(
                    plugin.getLanguage().get("list")
                        .replaceText(Lang.replace("%names%", String.join(", ", plugin.getMineManager().getMines().keySet())))
                );
                return Command.SINGLE_SUCCESS;
            })
            .build();
    }
}
