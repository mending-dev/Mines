package dev.mending.mines.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.mines.Mines;
import dev.mending.mines.command.ICommand;
import dev.mending.mines.mine.Mine;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class DeleteCommand implements ICommand {

    private final Mines plugin;

    public DeleteCommand(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("delete")
            .requires(sender -> sender.getSender().hasPermission("mines.command.delete"))
            .then(Commands.argument("name", StringArgumentType.word())
                .executes(ctx -> {
                    final String name = ctx.getArgument("name", String.class);

                    if (plugin.getMineManager().getMines().get(name) == null) {
                        ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("notFound").replaceText(Lang.replace("%name%", name)));
                        return Command.SINGLE_SUCCESS;
                    }

                    plugin.getMineManager().getMines().remove(name);
                    plugin.getMineManager().save();
                    ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("deleted").replaceText(Lang.replace("%name%", name)));

                    return Command.SINGLE_SUCCESS;
                })
            )
            .build();
    }
}
