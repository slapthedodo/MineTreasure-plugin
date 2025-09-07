package com.slapthedodo.treasurechests;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureChests extends JavaPlugin {

    private ProtocolManager protocolManager;
    private LootManager lootManager;
    private TreasureChestManager treasureChestManager;
    private DisplayManager displayManager;
    private MessageManager messageManager;
    private LuckBoosterManager luckBoosterManager;
    private ItemManager itemManager;
    private RewardManager rewardManager;
    private WorldGuardManager worldGuardManager;

    @Override
    public void onEnable() {
        // Save a copy of the default config.yml if one is not present
        saveDefaultConfig();

        // Initialize protocol manager
        if (getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        } else {
            getLogger().warning("ProtocolLib not found, phantom silencing will not work.");
            protocolManager = null;
        }

        // Initialize managers and handlers
        this.messageManager = new MessageManager(this);
        this.lootManager = new LootManager(this);
        this.treasureChestManager = new TreasureChestManager();
        this.displayManager = new DisplayManager(this, treasureChestManager);
        this.luckBoosterManager = new LuckBoosterManager(this);
        this.itemManager = new ItemManager(this);
        this.rewardManager = new RewardManager(this);
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            this.worldGuardManager = new WorldGuardManager();
        }

        // Load loot tables from config
        this.lootManager.loadLootTablesAsync();

        // Register event listeners
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this, lootManager, treasureChestManager), this);
        getServer().getPluginManager().registerEvents(new BarrierInteractListener(treasureChestManager), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this, treasureChestManager), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(treasureChestManager, this, displayManager), this);
        getServer().getPluginManager().registerEvents(new LuckBoosterListener(this), this);
        getServer().getPluginManager().registerEvents(new InfinityWaterBucketListener(this), this);
        getServer().getPluginManager().registerEvents(new InfinityGoldenCarrotListener(this), this);
        getServer().getPluginManager().registerEvents(new PhantomRepellerListener(this), this);
        if (protocolManager != null) {
            protocolManager.addPacketListener(new PhantomSoundListener(this));
        }

        // Register commands
        getCommand("givetreasureitem").setExecutor(new GiveTreasureItemCommand(this));
        getCommand("cybergiverandomspawner").setExecutor(new GiveSpawnerCommand(this));

        getLogger().info(messageManager.getPlainMessage("plugin-enabled"));
    }

    @Override
    public void onDisable() {
        getLogger().info(messageManager.getPlainMessage("plugin-disabled"));
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public LuckBoosterManager getLuckBoosterManager() {
        return luckBoosterManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }

    public TreasureChestManager getTreasureChestManager() {
        return treasureChestManager;
    }

    public LootManager getLootManager() {
        return lootManager;
    }

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public NamespacedKey getNamespacedKey(String key) {
        return new NamespacedKey(this, key);
    }
}
