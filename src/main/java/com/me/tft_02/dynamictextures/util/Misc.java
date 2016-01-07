package com.me.tft_02.dynamictextures.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.me.tft_02.dynamictextures.DynamicTextures;

public class Misc {

	public static Map<Player, Boolean> loadingStatus = new HashMap<Player, Boolean>();
	
    public static void loadResourcePack(Player player) {
        if (!player.hasPermission("dynamictextures.resourcepack")) {
            return;
        }

        String url;

        Location location = player.getLocation();
        String world = player.getWorld().getName().toLowerCase();
        //Bukkit.getLogger().info("getting URL of Worlds." + world);
        url = DynamicTextures.p.getConfig().getString("Worlds." + world);

        Set<String> texturePermissions = DynamicTextures.p.getConfig().getConfigurationSection("Permissions").getKeys(false);

        for (String name : texturePermissions) {
            if (player.hasPermission("dynamictextures." + name)) {
                String permission_url = DynamicTextures.p.getConfig().getString("Permissions." + name);

                if (isValidUrl(permission_url)) {
                	//Bukkit.getLogger().info("changing URL due to permission (" + name + ") to: " + permission_url);
                    url = permission_url;
                }
            }
        }

        if (DynamicTextures.p.getWorldGuardEnabled() && RegionUtils.isTexturedRegion(location)) {
            String region = RegionUtils.getRegion(location);

            if (!RegionUtils.getPreviousRegion(player).equals(region)) {
                RegionUtils.setPreviousRegion(player, region);
                url = RegionUtils.getRegionTexturePackUrl(region);
                //Bukkit.getLogger().info("changing URL due to WG region: " + region);
            }
        }

        if (isValidUrl(url)) {
        	//Bukkit.getLogger().info("loading " + url + " (putting player " + player.getName() + " into loading state)");
        	loadingStatus.put(player, true);
            player.setResourcePack(url);
        }
    }
    
    public static boolean isValidUrl(String url) {
        return ((url != null) && (url.contains("http://") || url.contains("https://")) && url.contains(".zip") && !url.contains("url_to_the_resource_pack_here"));
    }
}
