package com.me.tft_02.dynamictextures;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    DynamicTextures plugin;

    public Commands(DynamicTextures instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dynamictextures")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    return reloadConfiguration(sender);
                }
            }
            else {
                return printUsage(sender);
            }
        }
        return false;
    }

    private boolean printUsage(CommandSender sender) {
        sender.sendMessage("Usage: /dynamictextures [reload]");
        return false;
    }

    private boolean reloadConfiguration(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");

        if (sender instanceof Player) {
            Utils.loadTexturePack((Player) sender);
        }

        return false;
    }
}
