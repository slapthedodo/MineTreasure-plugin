package com.slapthedodo.treasurechests;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    private final TreasureChests plugin;

    public ItemManager(TreasureChests plugin) {
        this.plugin = plugin;
    }

    public ItemStack createLuckBooster(String tier) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("items.luck_boosters." + tier);
        if (section == null) {
            return null;
        }

        String materialName = section.getString("material", "YELLOW_DYE");
        Material material = Material.getMaterial(materialName.toUpperCase());
        if (material == null) {
            plugin.getLogger().warning("Invalid material '" + materialName + "' for luck booster tier '" + tier + "'. Defaulting to YELLOW_DYE.");
            material = Material.YELLOW_DYE;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("display-name")));

        List<String> lore = new ArrayList<>();
        for (String line : section.getStringList("lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("luck_booster_tier"), PersistentDataType.STRING, tier);

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createGoldenPickaxe() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("items.golden_pickaxe");
        if (section == null) {
            return null;
        }

        ItemStack item = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("display-name")));

        List<String> lore = new ArrayList<>();
        for (String line : section.getStringList("lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);

        for (String enchantment : section.getStringList("enchantments")) {
            String[] parts = enchantment.split(":");
            Enchantment ench = Enchantment.getByName(parts[0]);
            if (ench != null) {
                meta.addEnchant(ench, Integer.parseInt(parts[1]), true);
            }
        }

        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("golden_pickaxe"), PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createInfinityWaterBucket() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("items.infinity_water_bucket");
        if (section == null) {
            return null;
        }

        ItemStack item = new ItemStack(Material.WATER_BUCKET);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("display-name")));

        List<String> lore = new ArrayList<>();
        for (String line : section.getStringList("lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("infinity_water_bucket"), PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createInfinityGoldenCarrot() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("items.infinity_golden_carrot");
        if (section == null) {
            return null;
        }

        ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("display-name")));

        List<String> lore = new ArrayList<>();
        for (String line : section.getStringList("lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("infinity_golden_carrot"), PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createPhantomRepeller(String tier) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("items.phantom_repellers." + tier);
        if (section == null) {
            return null;
        }

        ItemStack item = new ItemStack(Material.PHANTOM_MEMBRANE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("display-name")));

        List<String> lore = new ArrayList<>();
        for (String line : section.getStringList("lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("phantom_repeller_tier"), PersistentDataType.STRING, tier);
        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("unique_id"), PersistentDataType.STRING, UUID.randomUUID().toString());

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createXpBottle(String tier) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("items.xp_flaschen_custom_xp." + tier);
        if (section == null) {
            return null;
        }

        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("display-name")));

        List<String> lore = new ArrayList<>();
        for (String line : section.getStringList("lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("xp_bottle_tier"), PersistentDataType.STRING, tier);
        meta.getPersistentDataContainer().set(plugin.getNamespacedKey("xp_bottle_amount"), PersistentDataType.INTEGER, section.getInt("xp"));

        item.setItemMeta(meta);
        return item;
    }
}
