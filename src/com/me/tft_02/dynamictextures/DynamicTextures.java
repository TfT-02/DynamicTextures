package com.me.tft_02.dynamictextures;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DynamicTextures extends JavaPlugin {

    private final PlayerListener playerListener = new PlayerListener(this);

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        setupConfiguration();
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();
        for (World world : getServer().getWorlds()) {
            config.addDefault("Worlds." + world.getName().toLowerCase(), "http://url_to_the_texture_pack_here");
        }
        config.addDefault("Permissions.custom_perm_name", "http://url_to_the_texture_pack_here");
        config.options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Run things on disable.
     */
    @Override
    public void onDisable() {
    }
}
