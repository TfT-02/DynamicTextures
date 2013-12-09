package com.me.tft_02.dynamictextures.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.me.tft_02.dynamictextures.DynamicTextures;
import com.me.tft_02.dynamictextures.util.Misc;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        DynamicTextures.p.getServer().getScheduler().scheduleSyncDelayedTask(DynamicTextures.p, new Runnable() {
            @Override
            public void run() {
                Misc.loadTexturePack(event.getPlayer());
            }
        }, 20);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        Misc.loadTexturePack(event.getPlayer());
    }
}
