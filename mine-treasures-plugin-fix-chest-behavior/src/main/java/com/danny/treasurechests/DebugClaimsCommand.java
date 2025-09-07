package com.danny.treasurechests;

import com.craftaro.ultimateclaims.member.ClaimPerm;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;

public class DebugClaimsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("ClaimPerm fields:");
        for (Field field : ClaimPerm.class.getFields()) {
            sender.sendMessage("- " + field.getName());
        }
        return true;
    }
}
