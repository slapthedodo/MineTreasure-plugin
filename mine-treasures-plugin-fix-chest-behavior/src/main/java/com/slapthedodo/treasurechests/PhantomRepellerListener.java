package com.slapthedodo.treasurechests;

import org.bukkit.entity.Entity;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PhantomRepellerListener implements Listener {

    private final TreasureChests plugin;

    public PhantomRepellerListener(TreasureChests plugin) {
        this.plugin = plugin;
        startPhantomCheck();
    }

    private void startPhantomCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    checkInventoryForRepeller(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Check every second
    }

    private void checkInventoryForRepeller(Player player) {
        PlayerInventory inventory = player.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.hasItemMeta()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                if (data.has(plugin.getNamespacedKey("phantom_repeller_tier"), PersistentDataType.STRING)) {
                    String tier = data.get(plugin.getNamespacedKey("phantom_repeller_tier"), PersistentDataType.STRING);
                    repelPhantoms(player, tier);
                    return; // Found a repeller, no need to check the rest of the inventory
                }
            }
        }
    }

    private void repelPhantoms(Player player, String tier) {
        double radius = plugin.getConfig().getDouble("items.phantom_repellers." + tier + ".repel-radius", 10.0);
        double pushForce = plugin.getConfig().getDouble("items.phantom_repellers." + tier + ".push-force", 1.5);
        String particleEffect = plugin.getConfig().getString("items.phantom_repellers." + tier + ".particle-effect", "CRIT");

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity.getType() == EntityType.PHANTOM) {
                Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                entity.setVelocity(direction.multiply(pushForce));

                try {
                    Particle particle = Particle.valueOf(particleEffect.toUpperCase());
                    player.getWorld().spawnParticle(particle, entity.getLocation(), 10, 0.5, 0.5, 0.5, 0);
                } catch (IllegalArgumentException e) {
                    // Particle effect not found, do nothing or log a warning
                }
            }
        }
    }
}
