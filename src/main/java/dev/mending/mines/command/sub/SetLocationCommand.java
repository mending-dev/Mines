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
import org.bukkit.entity.Player;

public class SetLocationCommand implements ICommand {

    private final Mines plugin;

    public SetLocationCommand(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("setlocation")
            .requires(sender -> sender.getSender().hasPermission("mines.command.setlocation"))
            .executes(ctx -> {
                if (ctx.getSource().getSender() instanceof Player player) {

                }
                return Command.SINGLE_SUCCESS;
            })
            .then(Commands.argument("name", StringArgumentType.word())
                    .executes(ctx -> {
                        if (ctx.getSource().getSender() instanceof Player player) {

                            final String name = ctx.getArgument("name", String.class);
                            final Mine mine = plugin.getMineManager().getMines().get(name);

                            if (mine == null) {
                                player.sendMessage(plugin.getLanguage().get("notFound").replaceText(Lang.replace("%name%", name)));
                                return Command.SINGLE_SUCCESS;
                            }

                            mine.setTeleportLocation(player.getLocation());
                            plugin.getMineManager().save();

                            player.sendMessage(plugin.getLanguage().get("locationSet").replaceText(Lang.replace("%name%", name)));
                        }
                        return Command.SINGLE_SUCCESS;
                    })
            )
            .build();
    }
}
