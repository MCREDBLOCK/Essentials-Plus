package com.redblock6.mccore.commands;

import me.invisibledrax.alliances.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class StreamingCommand implements Listener, CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command comd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = ((Player) sender).getPlayer();

        if (comd.getName().equalsIgnoreCase("streaming")) {
            if (Main.streaming) {
                Main.streaming = false;
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 100, 0);
                sender.sendMessage(Main.translate("&4&l> &fStreaming mode disabled."));
                for (Player loopplayer : Bukkit.getOnlinePlayers()) {
                    ((Player) sender).sendTitle(Main.translate("&4&lNO LONGER STREAMING"), Main.translate("&fAll non-whitelisted players will be kicked"));
                    if (!Bukkit.getWhitelistedPlayers().contains(loopplayer)) {
                        Bukkit.getServer().broadcastMessage(Main.translate("&4&l> &fKicked &c" + loopplayer.getDisplayName()));
                        loopplayer.kickPlayer(Main.translate("&fYou are not whitelisted on this server!"));
                    }
                }
            } else {
                Main.streaming = true;
                sender.sendMessage(Main.translate("&4&l> &fStreaming mode enabled!"));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 100, 2);
            }
        }
        return false;
    }
}
