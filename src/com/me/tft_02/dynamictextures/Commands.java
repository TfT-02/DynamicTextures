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
    
    String noPermission = ChatColor.DARK_RED + "You don't have permission to use this!";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dynamictextures")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    return reloadConfiguration(sender);
                }
                else if (args[0].equalsIgnoreCase("refreshall")) {
                    return refreshAllPlayers(sender);
                }
            }
            return printUsage(sender);
        }
        return false;
    }

    private boolean printUsage(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.GOLD + "DynamicTextures" + ChatColor.GRAY + " ]----- by " + ChatColor.GOLD + "TfT_02");

            String version = DynamicTextures.getInstance().getDescription().getVersion();
            String status = ChatColor.GREEN + "LATEST";
            if (plugin.updateAvailable) {
                status = ChatColor.RED + "OUTDATED";
            }

            player.sendMessage(ChatColor.GRAY + "Running version: "  + ChatColor.DARK_AQUA + version + " " + status);
            player.sendMessage("Usage: /dynamictextures [reload]");
            player.sendMessage("Usage: /dynamictextures [refreshall]");
        } else {
            sender.sendMessage("Usage: /dynamictextures [reload]");
            sender.sendMessage("Usage: /dynamictextures [refreshall]");
        }
        return true;
    }

    private boolean refreshAllPlayers(CommandSender sender) {
        if (!sender.hasPermission("dynamictextures.commands.refreshall")) {
            sender.sendMessage(noPermission);
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Refreshing textures for all players.");

        for (Player player : DynamicTextures.getInstance().getServer().getOnlinePlayers()) {
            Utils.loadTexturePack(player);
            player.sendMessage(ChatColor.GREEN + "Refreshing textures...");
        }

        return true;
    }

    private boolean reloadConfiguration(CommandSender sender) {
        if (!sender.hasPermission("dynamictextures.commands.reload")) {
            sender.sendMessage(noPermission);
            return false;
        }

        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");

        if (sender instanceof Player) {
            Utils.loadTexturePack((Player) sender);
        }

        return true;
    }
}
