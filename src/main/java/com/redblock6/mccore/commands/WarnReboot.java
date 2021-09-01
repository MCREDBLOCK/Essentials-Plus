package com.redblock6.mccore.commands;

import me.invisibledrax.alliances.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static me.invisibledrax.alliances.Main.translate;

public class WarnReboot implements CommandExecutor {
    private static final Main pl = Main.getInstance();
    private static int secondsTilRestart;

    //Code below by Ice922 (modified)
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warnreboot")) {
            if (!sender.hasPermission("redblock.warnreboot")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                return true;
            }
            if (args.length == 0) {
                startCountdown(15);
            } else {
                int minutes;
                try {
                    minutes = Integer.parseInt(args[0]);
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.RED + "Please enter a number of minutes!");
                    return true;
                }
                startCountdown(minutes);
            }
        }
        return false;
    }

    public static void startCountdown(int minutes) {
        secondsTilRestart = minutes * 60;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (secondsTilRestart == 15 * 60) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c15 minutes &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 10 * 60) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c10 minutes &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 5 * 60) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c5 minutes &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 4 * 60) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c4 minutes &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 3 * 60) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c3 minutes &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 2 * 60) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c2 minutes &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 1 * 60) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c1 minutes &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                    secondsTilRestart = 60;
                }
                if (secondsTilRestart == 45) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c45 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 30) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c30 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 15) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c15 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 10) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c10 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 9) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c9 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 8) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c8 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 7) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c7 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 6) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c6 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 5) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c5 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 4) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c4 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 3) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c3 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
                if (secondsTilRestart == 2) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c2 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 2);
                    }
                }
                if (secondsTilRestart == 1) {
                    Bukkit.getServer().broadcastMessage(translate("&4&l> &c1 seconds &funtil server restart"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 2);
                    }
                }
                if (secondsTilRestart == 0) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
                }
                secondsTilRestart--;
            }
        }.runTaskTimer(pl, 20, 20);
    }
}
