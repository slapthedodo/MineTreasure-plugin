package com.slapthedodo.treasurechests;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class PhantomSoundListener extends PacketAdapter {

    private final TreasureChests plugin;
    private final List<Sound> phantomSounds = Arrays.asList(
            Sound.ENTITY_PHANTOM_AMBIENT,
            Sound.ENTITY_PHANTOM_SWOOP,
            Sound.ENTITY_PHANTOM_FLAP,
            Sound.ENTITY_PHANTOM_BITE,
            Sound.ENTITY_PHANTOM_HURT
    );

    public PhantomSoundListener(TreasureChests plugin) {
        super(plugin, PacketType.Play.Server.NAMED_SOUND_EFFECT);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            Player player = event.getPlayer();
            Sound sound = event.getPacket().getSoundEffects().read(0);

            if (phantomSounds.contains(sound)) {
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.hasItemMeta()) {
                        if (item.getItemMeta().getPersistentDataContainer().has(plugin.getNamespacedKey("phantom_repeller_tier"), PersistentDataType.STRING)) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }
}
