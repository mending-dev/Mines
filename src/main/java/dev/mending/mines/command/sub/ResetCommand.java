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

public class ResetCommand implements ICommand {

    private final Mines plugin;

    public ResetCommand(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("reset")
            .then(Commands.literal("all")
                .executes(ctx -> {
                    plugin.getMineManager().getMines().forEach((name, mine) -> {
                        mine.reset();
                    });
                    ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("resetAll"));
                    return Command.SINGLE_SUCCESS;
                })
            )
            .then(Commands.argument("name", StringArgumentType.word())
                .executes(ctx -> {

                    final String name = ctx.getArgument("name", String.class);
                    final Mine mine = plugin.getMineManager().getMines().get(name);

                    if (mine == null) {
                        ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("notFound").replaceText(Lang.replace("%name%", name)));
                        return Command.SINGLE_SUCCESS;
                    }

                    mine.reset();
                    ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("resetSingle").replaceText(Lang.replace("%name%", name)));
                    return Command.SINGLE_SUCCESS;
                })
            )
            .build();
    }
}
