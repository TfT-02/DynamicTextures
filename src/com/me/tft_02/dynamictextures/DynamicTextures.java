package com.me.tft_02.dynamictextures;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class DynamicTextures extends JavaPlugin {
    public static DynamicTextures instance;

    private final PlayerListener playerListener = new PlayerListener(this);

    public boolean worldGuardEnabled = false;

    public static DynamicTextures getInstance() {
        return instance;
    }

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        instance = this;
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        setupConfiguration();
        setupWorldGuard();

        getCommand("dynamictextures").setExecutor(new Commands(this));

        BukkitScheduler scheduler = getServer().getScheduler();
        if (worldGuardEnabled) {
            //Region check timer (Runs every five seconds)
            scheduler.scheduleSyncRepeatingTask(this, new RegionTimer(this), 0, 5 * 20);
        }
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();
        for (World world : getServer().getWorlds()) {
            config.addDefault("Worlds." + world.getName().toLowerCase(), "http://url_to_the_texture_pack_here");
        }
        config.addDefault("Permissions.custom_perm_name", "http://url_to_the_texture_pack_here");
        config.addDefault("WorldGuard_Regions.region_name", "http://url_to_the_texture_pack_here");
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void setupWorldGuard() {
        if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardEnabled = true;
            getLogger().info("WorldGuard found!");
        }
    }

    WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    /**
     * Run things on disable.
     */
    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }
}
