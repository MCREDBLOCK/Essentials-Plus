package me.invisibledrax.alliances.truces;

import me.invisibledrax.alliances.Aplayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TruceCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("truce")) {
            if (args.length == 0) {
                for (String msg : getHelpMessage()) {
                    sender.sendMessage(msg);
                }
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can create truces!");
                return true;
            }
            Player p = (Player) sender;
            // Create truce
            if (args[0].equalsIgnoreCase("create")) {
                // Check if player is already in a truce
                if (Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You are already in a truce!");
                    return true;
                }
                // Check if player set a valid name
                if (args.length < 2 || args[1].length() < 3 || args[1].length() > 12) {
                    p.sendMessage(ChatColor.RED + "You must set a name between 3 and 12 characters long!");
                    return true;
                }
                if (Truce.exists(args[1])) {
                    p.sendMessage(ChatColor.RED + "A truce with this name already exists!");
                    return true;
                }
                Truce t = Truce.createTruce(args[1], p);
                p.sendMessage(ChatColor.AQUA + "Successfully created " + ChatColor.DARK_AQUA + args[1] + ChatColor.AQUA + " truce.");
            }
            if (args[0].equalsIgnoreCase("disband")) {
                // Check if player is in a truce
                if (!Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a truce to send that command!");
                    return true;
                }
                Truce truce = Truce.getTruce(p);
                // Check if player has permission to set truce description
                if (!truce.getRole(p).equals(TruceRole.Leader)) {
                    p.sendMessage(ChatColor.RED + "You must be the truce leader to send that command!");
                    return true;
                }
                for (OfflinePlayer off : truce.getMembers()) {
                    if (off.isOnline()) {
                        Player on = Bukkit.getPlayer(off.getName());
                        if (on.equals(p)) {
                            continue;
                        }
                        on.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!) Your truce has been disbanded by the owner!");
                    }
                }
                truce.disbandTruce();
                p.sendMessage(ChatColor.YELLOW + "Successfully disbanded truce!");
            }
            if (args[0].equalsIgnoreCase("info")) {
                if (args.length == 1) {
                    if (!Truce.isInTruce(p)) {
                        p.sendMessage(ChatColor.RED + "Please specify a truce!");
                        return true;
                    }
                    // Send info of players own truce
                    Truce truce = Truce.getTruce(p);
                    ArrayList<String> msgs = truce.getInfoMessage();
                    for (String s : msgs) {
                        p.sendMessage(s);
                    }
                }
                else {
                    // Send info of specified truce
                    if (!Truce.exists(args[1])) {
                        p.sendMessage(ChatColor.RED + "That truce does not exist!");
                        return true;
                    }
                    Truce truce = Truce.getTruce(args[1]);
                    ArrayList<String> msgs = truce.getInfoMessage();
                    for (String s : msgs) {
                        p.sendMessage(s);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("who")) {
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify whose truce you want to see!");
                    return true;
                }
                List<OfflinePlayer> offs = new ArrayList<>();
                for (OfflinePlayer off : Bukkit.getOfflinePlayers()) {
                    offs.add(off);
                }
                if (!offs.contains(Bukkit.getOfflinePlayer(args[1]))) {
                    p.sendMessage(ChatColor.RED + "Cannot find player or player does not exist!");
                    return true;
                }
                OfflinePlayer off = Bukkit.getOfflinePlayer(args[1]);
                if (!Truce.isInTruce(off)) {
                    p.sendMessage(ChatColor.YELLOW + "This player is not currently in a truce");
                    return true;
                }
                Truce truce = Truce.getTruce(off);
                ArrayList<String> msgs = truce.getInfoMessage();
                for(String msg : msgs) {
                    p.sendMessage(msg);
                }
            }
            if (args[0].equalsIgnoreCase("desc")) {
                // Check if player is in a truce
                if (!Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a truce to send that command!");
                    return true;
                }
                Truce truce = Truce.getTruce(p);
                // Check if player has permission to set truce description
                if (!truce.getRole(p).equals(TruceRole.Leader) && !truce.getRole(p).equals(TruceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "You must be a truce officer+ to send that command!");
                    return true;
                }
                if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
                    truce.setDescription(Truce.DEFAULT_DESCRIPTION);
                    p.sendMessage(ChatColor.YELLOW + "Successfully reset truce description!");
                    return true;
                }
                List<String> desc = new ArrayList<>();
                for (String arg : args) {
                    desc.add(arg);
                }
                desc.remove(0);
                String description = "";
                Iterator<String> it = desc.iterator();
                while (it.hasNext()) {
                    String s = it.next();
                    if (!it.hasNext()) {
                        description += s;
                        break;
                    }
                    description += s + " ";
                }
                if (description.length() > 50 || description.length() < 1) {
                    p.sendMessage(ChatColor.RED + "The description must be between 1 and 50 characters!");
                    return true;
                }
                truce.setDescription(description);
                p.sendMessage(ChatColor.YELLOW + "Successfully changed truce description!");
            }
            if (args[0].equalsIgnoreCase("invite")) {
                // Check if player is in a truce
                if (!Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a truce to send that command!");
                    return true;
                }
                Truce truce = Truce.getTruce(p);
                // Check if player has permission to invite players
                if (!truce.getRole(p).equals(TruceRole.Leader) && !truce.getRole(p).equals(TruceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "You must be a truce officer+ to send that command!");
                    return true;
                }
                if (truce.getMembers().size() >= Truce.MAX_MEMBERS) {
                    p.sendMessage(ChatColor.RED + "You already have the maximum ammount of players in your truce!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify who you would like to invite!");
                    return true;
                }
                if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
                    p.sendMessage(ChatColor.RED + "Cannot find player or player does not exist!");
                    return true;
                }
                Player invitee = Bukkit.getPlayer(args[1]);
                if (Truce.isInTruce(invitee)) {
                    p.sendMessage(ChatColor.RED + "This player is already in a truce!");
                    return true;
                }
                Aplayer.getAplayer(invitee).addTruceInvite(truce);
                p.sendMessage(ChatColor.YELLOW + "Successfully invited " + invitee.getName() + " to the truce!");
            }
            if (args[0].equalsIgnoreCase("join")) {
                if (Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You are already in a truce!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify the truce you would like to join!");
                    return true;
                }
                if (!Truce.exists(args[1])) {
                    p.sendMessage(ChatColor.RED + "This truce does not exist!");
                    return true;
                }
                Truce truce = Truce.getTruce(args[1]);
                Aplayer ap = Aplayer.getAplayer(p);
                if (!ap.hasTruceInvite(truce)) {
                    p.sendMessage(ChatColor.YELLOW + "You do not currently have any pending invites from this truce!");
                    return true;
                }
                for (BukkitTask task : ap.inviteRunnables) {
                    task.cancel();
                }
                ap.getTruceInvites().clear();
                for (Player member : truce.getOnlineMembers()) {
                    Bukkit.getPlayer(member.getUniqueId()).sendMessage(ChatColor.YELLOW + p.getName() + " has joined the truce!");
                }
                truce.addMember(p);
                p.sendMessage(ChatColor.YELLOW + "Successfully joined the " + truce.getName() + " truce!");
            }
            if (args[0].equalsIgnoreCase("promote")) {
                // Check if player is in a truce
                if (!Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a truce to send that command!");
                    return true;
                }
                Truce truce = Truce.getTruce(p);
                // Check if player has permission to set truce description
                if (!truce.getRole(p).equals(TruceRole.Leader)) {
                    p.sendMessage(ChatColor.RED + "You must be the truce leader to send that command!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify who you want to promote to officer!");
                    return true;
                }
                if (!truce.getMembers().contains(Bukkit.getOfflinePlayer(args[1]))) {
                    p.sendMessage(ChatColor.RED + "This player is not in your truce!");
                    return true;
                }
                OfflinePlayer off = Bukkit.getOfflinePlayer(args[1]);
                if (off.equals(p)) {
                    p.sendMessage(ChatColor.RED + "You cannot promote yourself!");
                    return true;
                }
                if (truce.getRole(off).equals(TruceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "This member is already an officer!");
                    return true;
                }
                truce.setRole(off, TruceRole.Officer);
                for (Player member : truce.getOnlineMembers()) {
                    if (member.equals(p)) {
                        continue;
                    }
                    if (member.equals(off)) {
                        member.sendMessage(ChatColor.YELLOW + "You have been promoted to Officer!");
                        continue;
                    }
                    member.sendMessage(ChatColor.YELLOW + off.getName() + " has been promoted to Officer!");
                }
                p.sendMessage(ChatColor.YELLOW + "Successfully promoted " + off.getName() + " to Officer!");
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (!Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You are not currently in a truce!");
                    return true;
                }
                Truce truce = Truce.getTruce(p);
                truce.removeMember(p);
                p.sendMessage(ChatColor.YELLOW + "You have left the " + truce.getName() + " truce!");
                for (Player member : truce.getOnlineMembers()) {
                    member.sendMessage(ChatColor.YELLOW + p.getName() + " has left the truce!");
                }
            }
            if (args[0].equalsIgnoreCase("kick")) {
                // Check if player is in a truce
                if (!Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a truce to send that command!");
                    return true;
                }
                Truce truce = Truce.getTruce(p);
                // Check if player has permission to set truce description
                if (!truce.getRole(p).equals(TruceRole.Leader) && !truce.getRole(p).equals(TruceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "You must be a truce officer+ to send that command!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify who you would like to kick!");
                    return true;
                }
                if (!truce.getMembers().contains(Bukkit.getPlayer(args[1]))) {
                    p.sendMessage(ChatColor.RED + "This player is not in your truce!");
                    return true;
                }
                OfflinePlayer member = Bukkit.getOfflinePlayer(args[1]);
                if (member.equals(p)) {
                    p.sendMessage(ChatColor.RED + "You cannot kick yourself from the truce!");
                    return true;
                }
                if (truce.getRole(p).equals(TruceRole.Officer) && truce.getRole(p).equals(TruceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "You must be the truce leader to kick and Officer!");
                    return true;
                }
                if (member.equals(truce.getLeader())) {
                    p.sendMessage(ChatColor.RED + "You cannot kick the Leader!");
                    return true;
                }
                for (Player on : truce.getOnlineMembers()) {
                    if (on.equals(p)) {
                        on.sendMessage(ChatColor.YELLOW + "Successfully kicked " + member.getName() + " from the truce!");
                        continue;
                    }
                    if (on.equals(member)) {
                        on.sendMessage(ChatColor.RED + p.getName() + " has kicked you from the truce!");
                        continue;
                    }
                    on.sendMessage(ChatColor.RED + p.getName() + " has kicked " + member.getName() + " from the truce!");
                }
                truce.removeMember(member);
            }
            if (args[0].equalsIgnoreCase("rename")) {
                // Check if player is in a truce
                if (!Truce.isInTruce(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a truce to send that command!");
                    return true;
                }
                Truce truce = Truce.getTruce(p);
                // Check if player has permission to set truce name
                if (!truce.getRole(p).equals(TruceRole.Leader)) {
                    p.sendMessage(ChatColor.RED + "You must be the truce leader to send that command!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify a new name!");
                    return true;
                }
                if (Truce.exists(args[1])) {
                    p.sendMessage(ChatColor.RED + "A truce with this name already exists!");
                    return true;
                }
                truce.changeName(args[1]);
                p.sendMessage(ChatColor.YELLOW + "Successfully changed truce name!");
                for (Player member : truce.getOnlineMembers()) {
                    if (member.equals(p)) {
                        continue;
                    }
                    member.sendMessage(ChatColor.YELLOW + "Your truce name has been changed to " + args[1] + "!");
                }
            }
            if (args[0].equalsIgnoreCase("help")) {
                for (String msg : getHelpMessage()) {
                    p.sendMessage(msg);
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("truce")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    ArrayList<String> subs = new ArrayList<>();
                    subs.add("create");
                    subs.add("disband");
                    subs.add("info");
                    subs.add("who");
                    subs.add("desc");
                    subs.add("invite");
                    subs.add("join");
                    subs.add("promote");
                    subs.add("leave");
                    subs.add("kick");
                    subs.add("rename");
                    subs.add("help");
                    StringUtil.copyPartialMatches(args[0], subs, completions);
                }
                else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("info")) {
                        ArrayList<String> names = new ArrayList<>();
                        for (Truce truce : Truce.getAllTruces()) {
                            names.add(truce.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("desc")) {
                        ArrayList<String> subs = new ArrayList<>();
                        subs.add("reset");
                        StringUtil.copyPartialMatches(args[1], subs, completions);
                    }
                    if (args[0].equalsIgnoreCase("who") || args[0].equalsIgnoreCase("invite")) {
                        ArrayList<String> names = new ArrayList<>();
                        for(Player on : Bukkit.getOnlinePlayers()) {
                            names.add(on.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("join")) {
                        ArrayList<String> names = new ArrayList<>();
                        Aplayer ap = Aplayer.getAplayer(p);
                        for(Truce t : ap.getTruceInvites()) {
                            names.add(t.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("promote")) {
                        ArrayList<String> names = new ArrayList<>();
                        for(OfflinePlayer off : Truce.getTruce(p).getMembers()) {
                            names.add(off.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("kick")) {
                        ArrayList<String> names = new ArrayList<>();
                        Truce truce = Truce.getTruce(p);
                        for(Player member : truce.getOnlineMembers()) {
                            names.add(member.getName());
                            if (member.equals(truce.getLeader())) {
                                continue;
                            }
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                }
            }
        }
        return completions;
    }

    public static ArrayList<String> getHelpMessage() {
        ArrayList<String> msgs = new ArrayList<>();
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l&m-----------------&r &e&lTruce Help&r &6&l&m-----------------"));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce create: &eCreates a truce that you own with the specified name."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce disband: &eDisbands your truce if you are the leader, kicking all the members."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce info: &eShows the info regarding the specified truce."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce who: &eShows the truce info of the specified player."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce desc: &eChanges the description of your truce."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce invite: &eInvites specified player to your truce. Invites expire after 2 minutes."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce join: &eJoins the specified truce if you are not already in one."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce promote: &ePromotes a regular member of the truce to an officer."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce rename: &eRenames the truce with the specified name."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce leave: &eLeaves your current truce."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/truce kick: &eKicks the specified player from your truce."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l&m---------------------------------------------"));
        return msgs;
    }

}
