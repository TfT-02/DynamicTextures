package com.me.tft_02.dynamictextures.util;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

import com.me.tft_02.dynamictextures.DynamicTextures;

import com.google.common.base.Charsets;
import net.minecraft.server.v1_7_R1.PacketPlayOutCustomPayload;
import org.apache.commons.lang.Validate;

public class Misc {

    public static void loadTexturePack(Player player) {
        if (!player.hasPermission("dynamictextures.change_texturepack")) {
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
            setTexturePack(player, url);
        }
    }

    public static void setTexturePack(Player player, String url) {
        boolean sendPackets = false;

        if (sendPackets) {
            try {
            Validate.notNull(url, "Resource pack URL cannot be null");

            byte[] message = url.getBytes(Charsets.UTF_8);
            Validate.isTrue(message.length <= Messenger.MAX_MESSAGE_SIZE, "Resource pack URL is too long");

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|RPack", message));
            }
            catch (Exception e) {
                DynamicTextures.p.getLogger().warning("Sending packets failed, plugin probably outdated!");
            }
        }
        else {
            player.setTexturePack(url);
        }
    }
}
