package com.me.tft_02.dynamictextures.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import com.me.tft_02.dynamictextures.DynamicTextures;
import com.me.tft_02.dynamictextures.runnables.LoadResourceTask;
import com.me.tft_02.dynamictextures.runnables.PlayerRemoveProtection;
import com.me.tft_02.dynamictextures.util.Misc;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	Misc.loadingStatus.put(p, false);
        new LoadResourceTask(p).runTaskLater(DynamicTextures.p, 20);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        Misc.loadResourcePack(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerResourcePackStatusEvent(PlayerResourcePackStatusEvent event) {
    	if (event.getStatus().name().equalsIgnoreCase("declined")) {
    		new PlayerRemoveProtection(event.getPlayer()).runTaskLater(DynamicTextures.p, 1);
    	} else if (!event.getStatus().name().equalsIgnoreCase("accepted")) {
    		new PlayerRemoveProtection(event.getPlayer()).runTaskLater(DynamicTextures.p, 200);
    	}
    }

    // prevent any damage and pushing of the player while loading a resource pack
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
    	if (event.getEntity() instanceof Player) {
	        if(Misc.loadingStatus.get((Player) event.getEntity()).equals(true)) {
	            event.setCancelled(true);
	        }
    	}
    }
}
