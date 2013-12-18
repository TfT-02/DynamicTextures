package com.me.tft_02.dynamictextures.util;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.me.tft_02.dynamictextures.DynamicTextures;

public class Misc {

    public static void loadResourcePack(Player player) {
        if (!player.hasPermission("dynamictextures.resourcepack")) {
            return;
        }

        String url;

        Location location = player.getLocation();
        String world = player.getWorld().getName().toLowerCase();
        url = DynamicTextures.p.getConfig().getString("Worlds." + world);

        String[] texturePermissions = DynamicTextures.p.getConfig().getConfigurationSection("Permissions").getKeys(false).toArray(new String[0]);

        for (String name : Arrays.asList(texturePermissions)) {
            if (player.hasPermission("dynamictextures." + name)) {
                String permission_url = DynamicTextures.p.getConfig().getString("Permissions." + name);
                if ((permission_url.contains("http://") || permission_url.contains("https://")) && permission_url.contains(".zip")) {
                    url = DynamicTextures.p.getConfig().getString("Permissions." + name);
                }
            }
        }

        if (DynamicTextures.p.getWorldGuardEnabled() && RegionUtils.isTexturedRegion(location)) {
            String region = RegionUtils.getRegion(location);

            if (!RegionUtils.getPreviousRegion(player).equals(region)) {
                RegionUtils.regionData.put(player.getName(), region);
                url = RegionUtils.getRegionTexturePackUrl(region);
            }
        }

        if ((url != null) && (url.contains("http://") || url.contains("https://")) && url.contains(".zip")) {
            player.setResourcePack(url);
        }
    }
}
