package com.danny.treasurechests;

import com.craftaro.ultimateclaims.UltimateClaims;
import com.craftaro.ultimateclaims.claim.ClaimManager;
import com.craftaro.ultimateclaims.member.ClaimPerm;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UltimateClaimsManager {

    private final ClaimManager claimManager;

    public UltimateClaimsManager() {
        this.claimManager = UltimateClaims.getInstance().getClaimManager();
    }

    public boolean canBuild(Player player, Location location) {
        // return claimManager.hasPermission(player, location, ClaimPerm.BUILD);
        return true;
    }
}
