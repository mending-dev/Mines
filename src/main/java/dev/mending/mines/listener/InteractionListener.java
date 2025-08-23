package dev.mending.mines.listener;

import dev.mending.core.paper.api.language.Lang;
import dev.mending.mines.Mines;
import dev.mending.mines.data.Item;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractionListener implements Listener {

    private final Mines plugin;

    public InteractionListener(Mines plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if (!e.getPlayer().hasPermission("mines.wand")) {
            return;
        }

        if (isItem(e.getItem()) && (e.getAction().isLeftClick() || e.getAction().isRightClick()) && e.getClickedBlock() != null) {
            if (e.getItem().equals(Item.WAND)) {
                switch (e.getAction()) {
                    case LEFT_CLICK_BLOCK -> {
                        e.setCancelled(true);
                        plugin.getSelectionCache().setPos1(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
                        e.getPlayer().sendMessage(plugin.getLanguage().get("positionSet").replaceText(Lang.replace("%pos%", "1")));
                    }
                    case RIGHT_CLICK_BLOCK -> {
                        e.setCancelled(true);
                        plugin.getSelectionCache().setPos2(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
                        e.getPlayer().sendMessage(plugin.getLanguage().get("positionSet").replaceText(Lang.replace("%pos%", "2")));
                    }
                }
            }
        }
    }

    public boolean isItem(ItemStack itemStack) {
        return itemStack != null && !itemStack.getType().equals(Material.AIR);
    }

}
