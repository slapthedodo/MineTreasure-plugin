package com.slapthedodo.treasurechests;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class XpBottleListener implements Listener {

    private final TreasureChests plugin;

    public XpBottleListener(TreasureChests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        if (data.has(plugin.getNamespacedKey("xp_bottle_tier"), PersistentDataType.STRING)) {
            event.setCancelled(true);

            int xpAmount = data.getOrDefault(plugin.getNamespacedKey("xp_bottle_amount"), PersistentDataType.INTEGER, 0);
            if (xpAmount > 0) {
                Player player = event.getPlayer();
                player.giveExp(xpAmount);
                item.setAmount(item.getAmount() - 1);
            }
        }
    }
}
