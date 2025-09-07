package com.danny.treasurechests;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    private final TreasureChests plugin;
    private final TreasureChestManager treasureChestManager;

    public BlockPlaceListener(TreasureChests plugin, TreasureChestManager treasureChestManager) {
        this.plugin = plugin;
        this.treasureChestManager = treasureChestManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        ItemStack itemInHand = event.getItemInHand();

        if (itemInHand.getType() == Material.SPAWNER && itemInHand.hasItemMeta()) {
            ItemMeta itemMeta = itemInHand.getItemMeta();
            if (itemMeta != null && itemMeta.getPersistentDataContainer().has(plugin.getNamespacedKey("spawner_type"), PersistentDataType.STRING)) {
                event.setCancelled(true);

                // Manually place the spawner and set its type
                block.setType(Material.SPAWNER);
                CreatureSpawner spawnerState = (CreatureSpawner) block.getState();
                String entityTypeName = itemMeta.getPersistentDataContainer().get(plugin.getNamespacedKey("spawner_type"), PersistentDataType.STRING);
                try {
                    EntityType entityType = EntityType.valueOf(entityTypeName);
                    spawnerState.setSpawnedType(entityType);
                    spawnerState.update(true);
                    plugin.getLogger().info("[DEBUG] Forcefully set spawner type to: " + entityType.name());

                    // Consume the item in hand
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid entity type for spawner: " + entityTypeName);
                    // Un-cancel the event to let the default spawner place
                    event.setCancelled(false);
                }
            }
        }

        // Only track blocks that are allowed to drop treasure chests
        java.util.List<String> allowedBlocks = plugin.getConfig().getStringList("allowed-blocks");
        if (allowedBlocks.contains(block.getType().name())) {
            treasureChestManager.addPlayerPlacedBlock(block.getLocation());
        }
    }
}
