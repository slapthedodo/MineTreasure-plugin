package com.danny.treasurechests;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
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
                final EquipmentSlot hand = event.getHand();
                final int slot = player.getInventory().getHeldItemSlot();

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (hand == EquipmentSlot.OFF_HAND) {
                        player.getInventory().setItemInOffHand(item);
                    } else {
                        player.getInventory().setItem(slot, item);
                    }
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                });
            }
        }
    }
}
