package com.me.tft_02.dynamictextures;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.me.tft_02.dynamictextures.commands.Commands;
import com.me.tft_02.dynamictextures.listeners.PlayerListener;
import com.me.tft_02.dynamictextures.runnables.RegionTimerTask;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.gravitydevelopment.updater.dynamictextures.Updater;
import net.gravitydevelopment.updater.dynamictextures.Updater.UpdateResult;
import net.gravitydevelopment.updater.dynamictextures.Updater.UpdateType;
import org.mcstats.Metrics;

public class DynamicTextures extends JavaPlugin {
    public static DynamicTextures p;
    public static File dynamictextures;

    public boolean worldGuardEnabled = false;

    // Update Check
    public boolean updateAvailable;

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        p = this;
        setupConfiguration();

        registerEvents();

        setupWorldGuard();
        setupFilePaths();

        getCommand("dynamictextures").setExecutor(new Commands());

        if (worldGuardEnabled) {
            //Region check timer (Runs every five seconds)
            new RegionTimerTask().runTaskTimer(this, 0, 5 * 20);
        }

        checkForUpdates();

        setupMetrics();
    }

    /**
     * Registers all event listeners
     */
    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Register events
        pluginManager.registerEvents(new PlayerListener(), this);
    }

    private void setupWorldGuard() {
        if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardEnabled = true;
            getLogger().info("WorldGuard found!");
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public boolean getWorldGuardEnabled() {
        return worldGuardEnabled;
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();
        config.addDefault("General.Stats_Tracking", true);
        config.addDefault("General.Update_Check", true);
        for (World world : getServer().getWorlds()) {
            config.addDefault("Worlds." + world.getName().toLowerCase(), "http://url_to_the_resource_pack_here");
        }
        config.addDefault("Permissions.custom_perm_name", "http://url_to_the_resource_pack_here");
        config.addDefault("WorldGuard_Regions.region_name", "http://url_to_the_resource_pack_here");
        config.options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Run things on disable.
     */
    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    /**
     * Setup the various storage file paths
     */
    private void setupFilePaths() {
        dynamictextures = getFile();
    }

    private void checkForUpdates() {
        if (!getConfig().getBoolean("General.Update_Check")) {
            return;
        }

        Updater updater = new Updater(this, 48782, dynamictextures, UpdateType.NO_DOWNLOAD, false);

        if (updater.getResult() != UpdateResult.UPDATE_AVAILABLE) {
            this.updateAvailable = false;
            return;
        }

        this.updateAvailable = true;
        this.getLogger().info(ChatColor.GOLD + "DynamicTextures is outdated!");
        this.getLogger().info(ChatColor.AQUA + "http://dev.bukkit.org/server-mods/worldtextures/");
    }

    private void setupMetrics() {
        if (!getConfig().getBoolean("General.Stats_Tracking")) {
            return;
        }

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e) {
            System.out.println("Failed to submit stats.");
        }
    }
}
