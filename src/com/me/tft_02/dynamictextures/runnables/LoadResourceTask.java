package com.me.tft_02.dynamictextures.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.dynamictextures.util.Misc;

public class LoadResourceTask extends BukkitRunnable {
    private Player player;

    public LoadResourceTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        Misc.loadTexturePack(player);
    }
}
