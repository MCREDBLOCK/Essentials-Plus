package com.redblock6.mccore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Gamemode implements Listener, CommandExecutor {

    public String gmc = "gmc";
    public String gms = "gms";
    public String gma = "gma";
    public String gmsp = "gmsp";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase(gmc)) {
                if (args.length < 1) {
                    if (sender.hasPermission("redblock.gamemode.c")) {
                        ((Player) sender).setGameMode(GameMode.CREATIVE);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYour gamemode has been set to &4&lCREATIVE"));
                        ((Player) sender).setAllowFlight(true);
                        ((Player) sender).setFlying(true);
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou don't have permission to change your gamemode to &ccreative&f. But we'll give you a joke: Why do we tell actors to &cbreak a leg&f? &fBecause every play has a &ccast&f."));
                    }
                } else {
                    if (sender.hasPermission("redblock.gamemode.c")) {
                        String sendername = ((Player) sender).getDisplayName();
                        for (Player loopplayer : Bukkit.getOnlinePlayers()) {
                            if (loopplayer.getName().equals(args[0])) {
                                loopplayer.setGameMode(GameMode.CREATIVE);
                                String loopplayername = loopplayer.getDisplayName();
                                loopplayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &c" + sendername + " &fset your gamemode to &4&lCREATIVE"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou set &c" + loopplayername + "'s &fgamemode to &4&lCREATIVE"));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou don't have permission to change your gamemode to &ccreative&f. But we'll give you a joke: Why do we tell actors to &cbreak a leg&f? &fBecause every play has a &ccast&f."));
                    }
                }
            } else if (cmd.getName().equalsIgnoreCase(gms)) {
                if (args.length < 1) {
                    if (sender.hasPermission("redblock.gamemode.s")) {
                        ((Player) sender).setGameMode(GameMode.SURVIVAL);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYour gamemode has been set to &4&lSURVIVAL"));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou don't have permission to change your gamemode to &csurvival&f. But we'll give you a joke: What do you call an elephant that doesn't matter? &fAn &cirrelifant&f."));
                    }
                } else {
                    if (sender.hasPermission("redblock.gamemode.s")) {
                        String sendername = ((Player) sender).getDisplayName();
                        for (Player loopplayer : Bukkit.getOnlinePlayers()) {
                            if (loopplayer.getName().equals(args[0])) {
                                loopplayer.setGameMode(GameMode.SURVIVAL);
                                String loopplayername = loopplayer.getDisplayName();
                                loopplayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &c" + sendername + " &fset your gamemode to &4&lSURVIVAL"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou set &c" + loopplayername + "'s &fgamemode to &4&lSURVIVAL"));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou don't have permission to change your gamemode to &csurvival&f. But we'll give you a joke: What do you call an elephant that doesn't matter? &fAn &cirrelifant&f."));
                    }
                }
            } else if (cmd.getName().equalsIgnoreCase(gma)) {
                if (args.length < 1) {
                    if (sender.hasPermission("redblock.gamemode.a")) {
                        ((Player) sender).setGameMode(GameMode.ADVENTURE);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYour gamemode has been set to &4&lADVENTURE"));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou don't have permission to change your gamemode to &cadventure&f. But we'll give you a joke: What rock group has four men that don't sing? &cMount Rushmore."));
                    }
                } else {
                    if (sender.hasPermission("redblock.gamemode.a")) {
                        for (Player loopplayer : Bukkit.getOnlinePlayers()) {
                            if (loopplayer.getName().equals(args[0])) {
                                loopplayer.setGameMode(GameMode.ADVENTURE);
                                loopplayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &c" + ((Player) sender).getDisplayName() + " &fset your gamemode to &4&lADVENTURE"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou set &c" + loopplayer.getDisplayName() + "'s &fgamemode to &4&lADVENTURE"));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou don't have permission to change your gamemode to &cadventure&f. But we'll give you a joke: What rock group has four men that don't sing? &cMount Rushmore."));
                    }
                }
            } else if (cmd.getName().equalsIgnoreCase(gmsp)) {
                if (args.length < 1) {
                    if (sender.hasPermission("redblock.gamemode.sp")) {
                        ((Player) sender).setGameMode(GameMode.SPECTATOR);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYour gamemode has been set to &4&lSPECTATOR"));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou don't have permission to change your gamemode to &cspectator&f. But we'll give you a joke: &fWhy couldn't the bicycle stand up by itself? It was &ctwo tired!"));
                    }
                } else {
                    if (sender.hasPermission("redblock.gamemode.sp")) {
                        for (Player loopplayer : Bukkit.getOnlinePlayers()) {
                            if (loopplayer.getName().equals(args[0])) {
                                loopplayer.setGameMode(GameMode.SPECTATOR);
                                loopplayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &c" + ((Player) sender).getDisplayName() + " &fset your gamemode to &4&lSPECTATOR"));
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou set &c" + loopplayer.getDisplayName() + "'s &fgamemode to &4&lSPECTATOR"));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fYou don't have permission to change your gamemode to &cspectator&f. But we'll give you a joke: &fWhy couldn't the bicycle stand up by itself? It was &ctwo tired!"));
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l> &fImagine trying to use &c/gm &fin console."));
            return true;
        }
        return false;
    }
}