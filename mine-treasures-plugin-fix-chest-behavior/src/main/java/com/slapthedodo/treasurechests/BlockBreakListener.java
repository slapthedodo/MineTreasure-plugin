package com.slapthedodo.treasurechests;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BlockBreakListener implements Listener {

    private final TreasureChests plugin;
    private final LootManager lootManager;
    private final TreasureChestManager treasureChestManager;

    public BlockBreakListener(TreasureChests plugin, LootManager lootManager, TreasureChestManager treasureChestManager) {
        this.plugin = plugin;
        this.lootManager = lootManager;
        this.treasureChestManager = treasureChestManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getLootManager().areLootTablesLoaded()) {
            return;
        }

        final Player player = event.getPlayer();
        final Location location = event.getBlock().getLocation();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (event.getBlock().getType() == Material.SPAWNER) {
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
                CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
                EntityType entityType = spawner.getSpawnedType();

                if (entityType != null) {
                    event.setDropItems(false);
                    event.setExpToDrop(0);

                    ItemStack spawnerItem = new ItemStack(Material.SPAWNER, 1);
                    ItemMeta meta = spawnerItem.getItemMeta();
                    if (meta != null) {
                        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("spawner_type"), PersistentDataType.STRING, entityType.name());
                        String entityName = entityType.name().substring(0, 1).toUpperCase() + entityType.name().substring(1).toLowerCase();
                        meta.setDisplayName(entityName + " Spawner");
                        spawnerItem.setItemMeta(meta);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), spawnerItem);
                    }
                }
            }
            return;
        }

        // Check if the block was placed by a player
        if (treasureChestManager.isPlayerPlacedBlock(location)) {
            treasureChestManager.removePlayerPlacedBlock(location); // Clean up the entry
            return;
        }

        if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().getPersistentDataContainer().has(plugin.getNamespacedKey("golden_pickaxe"), PersistentDataType.BOOLEAN)) {
            WorldGuardManager worldGuardManager = plugin.getWorldGuardManager();
            if (worldGuardManager != null && !worldGuardManager.canBuild(player, location)) {
                player.sendMessage(plugin.getMessageManager().getMessage("cannot-use-in-protected-area"));
                event.setCancelled(true);
                return;
            }


            event.setDropItems(false);
            itemInHand.setAmount(0);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final LootManager.LootResult lootResult = lootManager.calculateLootForTier("legendary");
                if (lootResult != null && !lootResult.getItems().isEmpty()) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        plugin.getDisplayManager().spawnTreasure(location, lootResult, player);
                        if (lootResult.getTier().isBroadcastEnabled()) {
                            String message = plugin.getMessageManager().getMessage("treasure-found", "%player%", player.getName(), "%tier%", lootResult.getTier().getDisplayName());
                            Bukkit.broadcastMessage(message);
                        }
                        SoundInfo soundInfo = lootResult.getTier().getSoundInfo();
                        String soundName = soundInfo.getName();
                        try {
                            if (soundInfo.shouldBroadcast()) {
                                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                    onlinePlayer.playSound(onlinePlayer.getLocation(), soundName, 1.0f, 1.0f);
                                }
                            } else {
                                player.playSound(player.getLocation(), soundName, 1.0f, 1.0f);
                            }
                        } catch (Exception e) {
                            plugin.getLogger().warning(plugin.getMessageManager().getMessage("sound-error", "%sound%", soundName));
                        }
                    });
                }
            });
            return;
        }

        // Check if the block is in the allowed list
        java.util.List<String> allowedBlocks = plugin.getConfig().getStringList("allowed-blocks");
        if (!allowedBlocks.contains(event.getBlock().getType().name())) {
            return;
        }

        // Check the drop chance
        double dropChance = plugin.getConfig().getDouble("drop-chance");
        if (new java.util.Random().nextDouble() >= dropChance) {
            return;
        }

        // Prevent the block from dropping its normal items
        event.setDropItems(false);

        // Asynchronously calculate the loot
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final LootManager.LootResult lootResult = lootManager.calculateRandomLoot(player);

            // If loot was successfully calculated, schedule the spawning back on the main thread
            if (lootResult != null && !lootResult.getItems().isEmpty()) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    // Spawn the treasure chest and custom model
                    plugin.getDisplayManager().spawnTreasure(location, lootResult, player);

                    // Send feedback to the server
                    if (lootResult.getTier().isBroadcastEnabled()) {
                        String message = plugin.getMessageManager().getMessage("treasure-found", "%player%", player.getName(), "%tier%", lootResult.getTier().getDisplayName());
                        Bukkit.broadcastMessage(message);
                    }

                    // Play sound based on tier settings
                    SoundInfo soundInfo = lootResult.getTier().getSoundInfo();
                    String soundName = soundInfo.getName();
                    try {
                        if (soundInfo.shouldBroadcast()) {
                            for (org.bukkit.entity.Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                onlinePlayer.playSound(onlinePlayer.getLocation(), soundName, 1.0f, 1.0f);
                            }
                        } else {
                            player.playSound(player.getLocation(), soundName, 1.0f, 1.0f);
                        }
                    } catch (Exception e) {
                        plugin.getLogger().warning(plugin.getMessageManager().getMessage("sound-error", "%sound%", soundName));
                    }
                });
            }
        });
    }
}
