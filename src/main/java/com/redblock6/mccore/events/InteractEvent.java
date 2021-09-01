package com.redblock6.mccore.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.invisibledrax.alliances.Main.translate;

public class InteractEvent implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.SPECTATOR)) {
            p.sendMessage(translate("&4&l> &fYou can't open containers as a spectator!"));
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
            for (Player lp : Bukkit.getOnlinePlayers()) {
                if (lp.hasPermission("redblock.admin")) {
                    lp.sendMessage(translate("&4&l> &c" + p.getName() + " &ftried to open a container!"));
                }
            }
            e.setCancelled(true);
        }
    }
}
