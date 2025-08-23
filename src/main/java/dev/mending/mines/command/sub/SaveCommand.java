package dev.mending.mines.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.mines.Mines;
import dev.mending.mines.command.ICommand;
import dev.mending.mines.mine.Mine;
import dev.mending.mines.selection.Selection;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class SaveCommand implements ICommand {

    private final Mines plugin;

    public SaveCommand(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("save")
            .requires(sender -> sender.getSender().hasPermission("mines.command.save"))
            .then(Commands.argument("name", StringArgumentType.word())
                .executes(ctx -> {
                    if (ctx.getSource().getSender() instanceof Player player) {

                        final String name = ctx.getArgument("name", String.class);
                        final Selection selection = plugin.getSelectionCache().get(player.getUniqueId());

                        if (!selection.isValid()) {
                            player.sendMessage(plugin.getLanguage().get("noPositionsFound"));
                            return Command.SINGLE_SUCCESS;
                        }

                        final Mine mine = new Mine(name, selection.pos1(), selection.pos2());

                        plugin.getMineManager().getMines().put(name, mine);
                        plugin.getMineManager().save();
                        player.sendMessage(plugin.getLanguage().get("saved").replaceText(Lang.replace("%name%", name)));
                    }
                    return Command.SINGLE_SUCCESS;
                })
            )
            .build();
    }
}
