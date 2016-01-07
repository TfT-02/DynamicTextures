package com.me.tft_02.dynamictextures.runnables;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.dynamictextures.DynamicTextures;
import com.me.tft_02.dynamictextures.util.Misc;
import com.me.tft_02.dynamictextures.util.RegionUtils;

public class RegionTimerTask extends BukkitRunnable {

    @Override
    public void run() {
        checkRegion();
    }

    public void checkRegion() {
        for (Player player : DynamicTextures.p.getServer().getOnlinePlayers()) {
            Location location = player.getLocation();

            if (!RegionUtils.isTexturedRegion(location, player)) {
                continue;
            }

            String region = RegionUtils.getRegion(location);

            if (RegionUtils.getPreviousRegion(player).equals(region)) {
                continue;
            }

            String url = RegionUtils.getRegionTexturePackUrl(region);

            if (!Misc.isValidUrl(url)) {
                DynamicTextures.p.getServer().getLogger().warning("Url for region: " + region + " resource pack is invalid: " + url);
                continue;
            }

            player.setResourcePack(url);
            RegionUtils.setPreviousRegion(player, region);
        }
    }
}
