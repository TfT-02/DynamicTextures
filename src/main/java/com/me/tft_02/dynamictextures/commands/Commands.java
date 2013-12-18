package com.me.tft_02.dynamictextures.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.me.tft_02.dynamictextures.DynamicTextures;
import com.me.tft_02.dynamictextures.util.Misc;

public class Commands implements CommandExecutor {
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
        sender.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.GOLD + "DynamicTextures" + ChatColor.GRAY + " ]----- by " + ChatColor.GOLD + "TfT_02");

        String version = DynamicTextures.p.getDescription().getVersion();
        String status = DynamicTextures.p.updateAvailable ? ChatColor.RED + "OUTDATED" : ChatColor.GREEN + "LATEST";

        sender.sendMessage(ChatColor.GRAY + "Running version: " + ChatColor.DARK_AQUA + version + " " + status);
        sender.sendMessage("Usage: /dynamictextures [reload]");
        sender.sendMessage("Usage: /dynamictextures [refreshall]");
        return true;
    }

    private boolean refreshAllPlayers(CommandSender sender) {
        if (!sender.hasPermission("dynamictextures.commands.refreshall")) {
            sender.sendMessage(noPermission);
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Refreshing textures for all players.");

        for (Player player : DynamicTextures.p.getServer().getOnlinePlayers()) {
            Misc.loadResourcePack(player);
            player.sendMessage(ChatColor.GREEN + "Refreshing textures...");
        }

        return true;
    }

    private boolean reloadConfiguration(CommandSender sender) {
        if (!sender.hasPermission("dynamictextures.commands.reload")) {
            sender.sendMessage(noPermission);
            return false;
        }

        DynamicTextures.p.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");

        if (sender instanceof Player) {
            Misc.loadResourcePack((Player) sender);
        }

        return true;
    }
}
