package com.danny.treasurechests;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InfinityGoldenCarrotListener implements Listener {

    private final TreasureChests plugin;

    public InfinityGoldenCarrotListener(TreasureChests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.GOLDEN_CARROT && item.hasItemMeta()) {
            if (item.getItemMeta().getPersistentDataContainer().has(plugin.getNamespacedKey("infinity_golden_carrot"), PersistentDataType.BOOLEAN)) {
                Player player = event.getPlayer();
                // We need to give the item back to the player a tick later
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    player.getInventory().setItem(event.getHand(), item);
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                });
            }
        }
    }
}
