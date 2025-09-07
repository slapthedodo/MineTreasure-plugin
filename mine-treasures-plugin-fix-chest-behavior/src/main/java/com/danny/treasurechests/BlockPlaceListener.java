package com.danny.treasurechests;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

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
            if (itemInHand.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta itemMeta = (BlockStateMeta) itemInHand.getItemMeta();
                if (itemMeta.getBlockState() instanceof CreatureSpawner) {
                    CreatureSpawner itemSpawnerState = (CreatureSpawner) itemMeta.getBlockState();
                    EntityType entityType = itemSpawnerState.getSpawnedType();

                    CreatureSpawner placedSpawnerState = (CreatureSpawner) block.getState();
                    placedSpawnerState.setSpawnedType(entityType);
                    placedSpawnerState.update();
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
