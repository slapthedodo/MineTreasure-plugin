package com.slapthedodo.treasurechests;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GiveSpawnerCommand implements CommandExecutor {

    private final TreasureChests plugin;
    private final List<String> spawnerTypes = Arrays.asList(
            "ZOMBIE", "SKELETON", "SPIDER", "CAVE_SPIDER", "CREEPER", "SLIME", "WITCH", "PIGLIN", "BLAZE",
            "PIG", "COW", "CHICKEN", "SHEEP", "DROWNED", "ENDERMAN", "GLOW_SQUID", "GUARDIAN", "HOGLIN",
            "HUSK", "IRON_GOLEM", "MAGMA_CUBE", "MOOSHROOM", "PILLAGER", "POLAR_BEAR", "RABBIT", "STRAY",
            "STRIDER", "SQUID", "TURTLE", "ZOGLIN"
    );
    private final Random random = new Random();

    public GiveSpawnerCommand(TreasureChests plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp() && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            sender.sendMessage(plugin.getMessageManager().getMessage("no-permission"));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(plugin.getMessageManager().getMessage("givespawner-usage"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.getMessageManager().getMessage("player-not-found"));
            return true;
        }

        String randomType = spawnerTypes.get(random.nextInt(spawnerTypes.size()));
        ItemStack spawner = new ItemStack(Material.SPAWNER, 1);
        ItemMeta meta = spawner.getItemMeta();
        if (meta != null) {
            try {
                EntityType entityType = EntityType.valueOf(randomType.toUpperCase());
                meta.setDisplayName(randomType.substring(0, 1).toUpperCase() + randomType.substring(1).toLowerCase() + " Spawner");
                meta.getPersistentDataContainer().set(plugin.getNamespacedKey("spawner_type"), PersistentDataType.STRING, entityType.name());
                spawner.setItemMeta(meta);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid entity type for spawner: " + randomType);
                sender.sendMessage("Error: Invalid entity type '" + randomType + "'.");
                return true;
            }
        }

        target.getInventory().addItem(spawner);
        sender.sendMessage(plugin.getMessageManager().getMessage("spawner-given", "%player%", target.getName()));

        return true;
    }
}
