package com.danny.treasurechests;

import org.bukkit.Material;

import java.util.List;

public class LootItem {

    private final Material material;
    private final String customItem;
    private final String displayName;
    private final String amount;
    private final double chance;
    private final List<String> potionEffects;
    private final List<String> spawnerTypes;

    public LootItem(Material material, String customItem, String displayName, String amount, double chance, List<String> potionEffects, List<String> spawnerTypes) {
        this.material = material;
        this.customItem = customItem;
        this.displayName = displayName;
        this.amount = amount;
        this.chance = chance;
        this.potionEffects = potionEffects;
        this.spawnerTypes = spawnerTypes;
    }

    public Material getMaterial() {
        return material;
    }

    public String getCustomItem() {
        return customItem;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAmount() {
        return amount;
    }

    public double getChance() {
        return chance;
    }

    public List<String> getPotionEffects() {
        return potionEffects;
    }

    public List<String> getSpawnerTypes() {
        return spawnerTypes;
    }
}
