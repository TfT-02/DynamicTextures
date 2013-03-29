package com.me.tft_02.dynamictextures;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Utils {

    public static void loadTexturePack(Player player) {
        if (!player.hasPermission("dynamictextures.change_texturepack")) {
            return;
        }

        String url;

        Location location = player.getLocation();
        String world = player.getWorld().getName().toLowerCase();
        url = DynamicTextures.getInstance().getConfig().getString("Worlds." + world);

        String[] texturePermissions = DynamicTextures.getInstance().getConfig().getConfigurationSection("Permissions").getKeys(false).toArray(new String[0]);

        for (String name : Arrays.asList(texturePermissions)) {
            if (player.hasPermission("dynamictextures." + name)) {
                String permission_url = DynamicTextures.getInstance().getConfig().getString("Permissions." + name);
                if ((permission_url.contains("http://") || permission_url.contains("https://")) && permission_url.contains(".zip")) {
                    url = DynamicTextures.getInstance().getConfig().getString("Permissions." + name);
                }
            }
        }

        if (DynamicTextures.getInstance().worldGuardEnabled && RegionUtils.isTexturedRegion(location)) {
            url = RegionUtils.getRegionTexturePackUrl(RegionUtils.getRegion(location));
        }

        if ((url != null) && (url.contains("http://") || url.contains("https://")) && url.contains(".zip")) {
            player.setTexturePack(url);
        }
    }
}
