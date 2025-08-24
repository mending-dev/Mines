package dev.mending.mines.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mending.core.paper.api.language.Lang;
import dev.mending.mines.Mines;
import dev.mending.mines.command.ICommand;
import dev.mending.mines.gui.ContentGui;
import dev.mending.mines.mine.Mine;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class SetCommand implements ICommand {

    private final Mines plugin;

    public SetCommand(Mines plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("set")
            .requires(sender -> sender.getSender().hasPermission("mines.command.set"))
            .then(Commands.literal("interval")
                .then(Commands.argument("name", StringArgumentType.word())
                    .then(Commands.argument("seconds", LongArgumentType.longArg(1))
                        .executes(ctx -> {

                            final String name = ctx.getArgument("name", String.class);
                            final Long seconds = ctx.getArgument("seconds", Long.class);
                            final Mine mine = plugin.getMineManager().getMines().get(name);

                            if (mine == null) {
                                ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("notFound").replaceText(Lang.replace("%name%", name)));
                                return Command.SINGLE_SUCCESS;
                            }

                            mine.setResetInterval(seconds);
                            plugin.getMineManager().save();
                            plugin.getMineManager().reload();
                            ctx.getSource().getSender().sendMessage(plugin.getLanguage().get("intervalSet")
                                .replaceText(Lang.replace("%name%", name))
                                .replaceText(Lang.replace("%interval%", "" + seconds))
                            );

                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
            )
            .then(Commands.literal("location")
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
            )
            .then(Commands.literal("content")
                .then(Commands.argument("name", StringArgumentType.word())
                    .executes(ctx -> {
                        if (ctx.getSource().getSender() instanceof Player player) {

                            final String name = ctx.getArgument("name", String.class);
                            final Mine mine = plugin.getMineManager().getMines().get(name);

                            if (mine == null) {
                                player.sendMessage(plugin.getLanguage().get("notFound").replaceText(Lang.replace("%name%", name)));
                                return Command.SINGLE_SUCCESS;
                            }

                            new ContentGui(plugin, name, null).open(player);
                        }
                        return Command.SINGLE_SUCCESS;
                    })
                )
            )
            .build();
    }
}
