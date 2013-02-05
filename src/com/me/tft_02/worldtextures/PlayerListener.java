package com.me.tft_02.worldtextures;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
	WorldTextures plugin;

	public PlayerListener(final WorldTextures instance) {
		plugin = instance;
	}

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		loadTexturePack(event.getPlayer());
	}

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		loadTexturePack(event.getPlayer());
	}

	private void loadTexturePack(Player player) {
		String world = player.getWorld().getName().toLowerCase();
		String url = plugin.getConfig().getString(world);
		if (url != null) {
			if (url.contains("http://") || url.contains("https://")) {
				if (player.hasPermission("worldtextures.change_texturepack")) {
					player.setTexturePack(url);
				}
			}
			else 
				plugin.getLogger().log(Level.WARNING, " Could not change texturepack for " + player.getName() + " in "+ world + ", incorrect url!");
		}
	}
}
