package com.redblock6.mccore.events;

import me.invisibledrax.alliances.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.invisibledrax.alliances.Main.streaming;

public class JoinLeaveEvent implements Listener {

    /*
    @EventHandler
    public void itemDurability(PlayerItemDamageEvent e) {
        Player p = e.getPlayer();
        if (p.getName().equals("RiverGod16")) {
            if (e.getItem().getType().equals(Material.TRIDENT)) {
                e.setCancelled(true);
            }
        }
    }

     */

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!Bukkit.getWhitelistedPlayers().contains(p)) {
            p.kickPlayer("You are not whitelisted on this server!");
        }

        /*
        if (p.getName().equals("hotcloud")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (day()) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1000000000, 0));
                    } else {
                        p.removePotionEffect(PotionEffectType.WEAKNESS);
                    }
                }
            }.runTaskTimer(Main.getInstance(), 10, 120);

            if (day()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1000000000, 0));
            } else {
                p.removePotionEffect(PotionEffectType.WEAKNESS);
            }

         */

        if (streaming) {
            if (!Bukkit.getWhitelistedPlayers().contains(p)) {
                p.setGameMode(GameMode.SPECTATOR);
                p.sendTitle(Main.translate("&2&lSPECTATING"), Main.translate("&fYou are a spectator!"), 10, 20, 10);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
                e.setJoinMessage(Main.translate("&2&l> &a" + p.getDisplayName() + " &fis now spectating."));
            } else {
                p.sendTitle(Main.translate("&2&lSOMEONE IS STREAMING"), Main.translate("&fStreamer mode is enabled."), 10, 20, 10);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
            }
        } else {
            if (!Bukkit.getWhitelistedPlayers().contains(p)) {
                p.kickPlayer("You are not whitelisted on this server!");
            }
        }


    }



    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        /*
        if (streaming) {
            if (!Bukkit.getWhitelistedPlayers().contains(p)) {
                e.setQuitMessage(translate("&2&l> &a" + p.getName() + " &fis no longer spectating."));
            }
        }

         */
    }
}
