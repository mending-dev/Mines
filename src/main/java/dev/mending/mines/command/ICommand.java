package dev.mending.mines.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface ICommand {

    LiteralCommandNode<CommandSourceStack> get();
}
