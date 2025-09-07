package com.slapthedodo.treasurechests;

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

        if (block.getType() == Material.SPAWNER && itemInHand.hasItemMeta()) {
            ItemMeta itemMeta = itemInHand.getItemMeta();
            if (itemMeta != null && itemMeta.getPersistentDataContainer().has(plugin.getNamespacedKey("spawner_type"), PersistentDataType.STRING)) {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    String entityTypeName = itemMeta.getPersistentDataContainer().get(plugin.getNamespacedKey("spawner_type"), PersistentDataType.STRING);
                    try {
                        EntityType entityType = EntityType.valueOf(entityTypeName);
                        CreatureSpawner placedSpawnerState = (CreatureSpawner) block.getState();
                        placedSpawnerState.setSpawnedType(entityType);
                        placedSpawnerState.update(true);
                        plugin.getLogger().info("[DEBUG] Delayed spawner type set to: " + entityType.name());
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Invalid entity type for spawner: " + entityTypeName);
                    }
                }, 1L);
            }
        }

        // Only track blocks that are allowed to drop treasure chests
        java.util.List<String> allowedBlocks = plugin.getConfig().getStringList("allowed-blocks");
        if (allowedBlocks.contains(block.getType().name())) {
            treasureChestManager.addPlayerPlacedBlock(block.getLocation());
        }
    }
}
