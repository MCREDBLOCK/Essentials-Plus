package me.invisibledrax.alliances.alliances;

import me.invisibledrax.alliances.Aplayer;
import me.invisibledrax.alliances.Main;
import me.invisibledrax.alliances.filter.AllowedCharacters;
import me.invisibledrax.alliances.filter.BlacklistedWords;
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

public class AllianceCommand implements CommandExecutor, TabExecutor {

    private static String commandName = Main.commandName;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(Main.commandName)) {
            if (args.length == 0) {
                for (String msg : getHelpMessage()) {
                    sender.sendMessage(msg);
                }
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can create " + commandName + "s!");
                return true;
            }
            Player p = (Player) sender;
            // Create truce
            if (args[0].equalsIgnoreCase("create")) {
                // Check if player is already in a truce
                if (Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You are already in an " + commandName + "!");
                    return true;
                }
                // Check if player set a valid name
                if (args.length < 2 || args[1].length() < 3 || args[1].length() > 12) {
                    p.sendMessage(ChatColor.RED + "You must set a name between 3 and 12 characters long!");
                    return true;
                }
                if (!AllowedCharacters.allowed(args[1])) {
                    p.sendMessage(ChatColor.RED + "You must set a name using only alphanumeric characters!");
                    return true;
                }
                if (!BlacklistedWords.allowed(args[1])) {
                    p.sendMessage(ChatColor.RED + "Please refrain from using vulgar in your " + commandName + " name!");
                    return true;
                }
                if (Alliance.exists(args[1])) {
                    p.sendMessage(ChatColor.RED + "A " + commandName + " with this name already exists!");
                    return true;
                }
                Alliance t = Alliance.createAlliance(args[1], p);
                p.sendMessage(ChatColor.AQUA + "Successfully created " + ChatColor.DARK_AQUA + args[1] + ChatColor.AQUA + " " + commandName + ".");
            }
            if (args[0].equalsIgnoreCase("disband")) {
                // Check if player is in a truce
                if (!Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a " + commandName + " to send that command!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(p);
                // Check if player has permission to set truce description
                if (!alliance.getRole(p).equals(AllianceRole.Leader)) {
                    p.sendMessage(ChatColor.RED + "You must be the " + commandName + " leader to send that command!");
                    return true;
                }
                for (OfflinePlayer off : alliance.getMembers()) {
                    if (off.isOnline()) {
                        Player on = Bukkit.getPlayer(off.getName());
                        if (on.equals(p)) {
                            continue;
                        }
                        on.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!) Your " + commandName + " has been disbanded by the " +
                                "owner!");
                    }
                }
                alliance.disbandAlliance();
                p.sendMessage(ChatColor.YELLOW + "Successfully disbanded " + commandName + "!");
            }
            if (args[0].equalsIgnoreCase("info")) {
                if (args.length == 1) {
                    if (!Alliance.isInAlliance(p)) {
                        p.sendMessage(ChatColor.RED + "Please specify a " + commandName + "!");
                        return true;
                    }
                    // Send info of players own truce
                    Alliance alliance = Alliance.getAlliance(p);
                    ArrayList<String> msgs = alliance.getInfoMessage();
                    for (String s : msgs) {
                        p.sendMessage(s);
                    }
                } else {
                    // Send info of specified truce
                    if (!Alliance.exists(args[1])) {
                        p.sendMessage(ChatColor.RED + "That " + commandName + " does not exist!");
                        return true;
                    }
                    Alliance alliance = Alliance.getAlliance(args[1]);
                    ArrayList<String> msgs = alliance.getInfoMessage();
                    for (String s : msgs) {
                        p.sendMessage(s);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("who")) {
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify whose " + commandName + " you want to see!");
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
                if (!Alliance.isInAlliance(off)) {
                    p.sendMessage(ChatColor.YELLOW + "This player is not currently in a " + commandName + "!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(off);
                ArrayList<String> msgs = alliance.getInfoMessage();
                for (String msg : msgs) {
                    p.sendMessage(msg);
                }
            }
            if (args[0].equalsIgnoreCase("desc")) {
                // Check if player is in a truce
                if (!Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a " + commandName + " to send that command!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(p);
                // Check if player has permission to set truce description
                if (!alliance.getRole(p).equals(AllianceRole.Leader) && !alliance.getRole(p).equals(AllianceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "You must be a " + commandName + " officer+ to send that command!");
                    return true;
                }
                if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
                    alliance.setDescription(Alliance.DEFAULT_DESCRIPTION);
                    p.sendMessage(ChatColor.YELLOW + "Successfully reset " + commandName + " description!");
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
                alliance.setDescription(description);
                p.sendMessage(ChatColor.YELLOW + "Successfully changed " + commandName + " description!");
            }
            if (args[0].equalsIgnoreCase("invite")) {
                // Check if player is in a truce
                if (!Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a " + commandName + " to send that command!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(p);
                // Check if player has permission to invite players
                if (!alliance.getRole(p).equals(AllianceRole.Leader) && !alliance.getRole(p).equals(AllianceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "You must be a " + commandName + " officer+ to send that command!");
                    return true;
                }
                if (alliance.getMembers().size() >= Alliance.MAX_MEMBERS) {
                    p.sendMessage(ChatColor.RED + "You already have the maximum ammount of players in your " + commandName + "!");
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
                if (Alliance.isInAlliance(invitee)) {
                    p.sendMessage(ChatColor.RED + "This player is already in a " + commandName + "!");
                    return true;
                }
                Aplayer.getAplayer(invitee).addTruceInvite(alliance);
                p.sendMessage(ChatColor.YELLOW + "Successfully invited " + invitee.getName() + " to the " + commandName + "!");
            }
            if (args[0].equalsIgnoreCase("join")) {
                if (Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You are already in a " + commandName + "!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify the " + commandName + " you would like to join!");
                    return true;
                }
                if (!Alliance.exists(args[1])) {
                    p.sendMessage(ChatColor.RED + "This " + commandName + " does not exist!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(args[1]);
                Aplayer ap = Aplayer.getAplayer(p);
                if (!ap.hasTruceInvite(alliance)) {
                    p.sendMessage(ChatColor.YELLOW + "You do not currently have any pending invites from this " + commandName + "!");
                    return true;
                }
                for (BukkitTask task : ap.inviteRunnables) {
                    task.cancel();
                }
                ap.getTruceInvites().clear();
                for (Player member : alliance.getOnlineMembers()) {
                    Bukkit.getPlayer(member.getUniqueId()).sendMessage(ChatColor.YELLOW + p.getName() + " has joined the " + commandName + "!");
                }
                alliance.addMember(p);
                p.sendMessage(ChatColor.YELLOW + "Successfully joined the " + alliance.getName() + " " + commandName + "!");
            }
            if (args[0].equalsIgnoreCase("promote")) {
                // Check if player is in a truce
                if (!Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a " + commandName + " to send that command!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(p);
                // Check if player has permission to set truce description
                if (!alliance.getRole(p).equals(AllianceRole.Leader)) {
                    p.sendMessage(ChatColor.RED + "You must be the " + commandName + " leader to send that command!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify who you want to promote to officer!");
                    return true;
                }
                if (!alliance.getMembers().contains(Bukkit.getOfflinePlayer(args[1]))) {
                    p.sendMessage(ChatColor.RED + "This player is not in your " + commandName + "!");
                    return true;
                }
                OfflinePlayer off = Bukkit.getOfflinePlayer(args[1]);
                if (off.equals(p)) {
                    p.sendMessage(ChatColor.RED + "You cannot promote yourself!");
                    return true;
                }
                if (alliance.getRole(off).equals(AllianceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "This member is already an officer!");
                    return true;
                }
                alliance.setRole(off, AllianceRole.Officer);
                for (Player member : alliance.getOnlineMembers()) {
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
                if (!Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You are not currently in a " + commandName + "!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(p);
                alliance.removeMember(p);
                p.sendMessage(ChatColor.YELLOW + "You have left the " + alliance.getName() + " " + commandName + "!");
                for (Player member : alliance.getOnlineMembers()) {
                    member.sendMessage(ChatColor.YELLOW + p.getName() + " has left the " + commandName + "!");
                }
            }
            if (args[0].equalsIgnoreCase("kick")) {
                // Check if player is in a truce
                if (!Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a " + commandName + " to send that command!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(p);
                // Check if player has permission to set truce description
                if (!alliance.getRole(p).equals(AllianceRole.Leader) && !alliance.getRole(p).equals(AllianceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "You must be a " + commandName + " officer+ to send that command!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify who you would like to kick!");
                    return true;
                }
                if (!alliance.getMembers().contains(Bukkit.getPlayer(args[1]))) {
                    p.sendMessage(ChatColor.RED + "This player is not in your " + commandName + "!");
                    return true;
                }
                OfflinePlayer member = Bukkit.getOfflinePlayer(args[1]);
                if (member.equals(p)) {
                    p.sendMessage(ChatColor.RED + "You cannot kick yourself from the " + commandName + "!");
                    return true;
                }
                if (alliance.getRole(p).equals(AllianceRole.Officer) && alliance.getRole(p).equals(AllianceRole.Officer)) {
                    p.sendMessage(ChatColor.RED + "You must be the " + commandName + " leader to kick and Officer!");
                    return true;
                }
                if (member.equals(alliance.getLeader())) {
                    p.sendMessage(ChatColor.RED + "You cannot kick the Leader!");
                    return true;
                }
                for (Player on : alliance.getOnlineMembers()) {
                    if (on.equals(p)) {
                        on.sendMessage(ChatColor.YELLOW + "Successfully kicked " + member.getName() + " from the " + commandName + "!");
                        continue;
                    }
                    if (on.equals(member)) {
                        on.sendMessage(ChatColor.RED + p.getName() + " has kicked you from the " + commandName + "!");
                        continue;
                    }
                    on.sendMessage(ChatColor.RED + p.getName() + " has kicked " + member.getName() + " from the " + commandName + "!");
                }
                alliance.removeMember(member);
            }
            if (args[0].equalsIgnoreCase("rename")) {
                // Check if player is in a truce
                if (!Alliance.isInAlliance(p)) {
                    p.sendMessage(ChatColor.RED + "You must be in a " + commandName + " to send that command!");
                    return true;
                }
                Alliance alliance = Alliance.getAlliance(p);
                // Check if player has permission to set truce name
                if (!alliance.getRole(p).equals(AllianceRole.Leader)) {
                    p.sendMessage(ChatColor.RED + "You must be the " + commandName + " leader to send that command!");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please specify a new name!");
                    return true;
                }
                if (Alliance.exists(args[1])) {
                    p.sendMessage(ChatColor.RED + "A " + commandName + " with this name already exists!");
                    return true;
                }
                alliance.changeName(args[1]);
                p.sendMessage(ChatColor.YELLOW + "Successfully changed " + commandName + " name!");
                for (Player member : alliance.getOnlineMembers()) {
                    if (member.equals(p)) {
                        continue;
                    }
                    member.sendMessage(ChatColor.YELLOW + "Your " + commandName + " name has been changed to " + args[1] + "!");
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
        if (command.getName().equalsIgnoreCase(commandName)) {
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
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("info")) {
                        ArrayList<String> names = new ArrayList<>();
                        for (Alliance alliance : Alliance.getAllAlliances()) {
                            names.add(alliance.getName());
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
                        for (Player on : Bukkit.getOnlinePlayers()) {
                            names.add(on.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("join")) {
                        ArrayList<String> names = new ArrayList<>();
                        Aplayer ap = Aplayer.getAplayer(p);
                        for (Alliance t : ap.getTruceInvites()) {
                            names.add(t.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("promote")) {
                        ArrayList<String> names = new ArrayList<>();
                        for (OfflinePlayer off : Alliance.getAlliance(p).getMembers()) {
                            names.add(off.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("kick")) {
                        ArrayList<String> names = new ArrayList<>();
                        Alliance alliance = Alliance.getAlliance(p);
                        for (Player member : alliance.getOnlineMembers()) {
                            names.add(member.getName());
                            if (member.equals(alliance.getLeader())) {
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
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " create: &eCreates a " + commandName + "that you own" +
                " with the specified name."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " disband: &eDisbands your " + commandName + "if you " +
                "are the leader, kicking all the members."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " info: &eShows the info regarding the specified " +
                commandName + "."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " who: &eShows the " + commandName + "info of the " +
                "specified " + "player" + "."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " desc: &eChanges the description of your " +
                commandName +
                "."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " invite: &eInvites specified player to your " +
                commandName + ". Invites expire after 2 minutes."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " join: &eJoins the specified " + commandName + "if " +
                "you are not already in one."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " promote: &ePromotes a regular member of the " +
                commandName + " to an officer."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " rename: &eRenames the " + commandName + "with the " +
                "specified name."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " leave: &eLeaves your current " + commandName + ""));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l/" + commandName + " kick: &eKicks the specified player from your " +
                commandName + "."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&6&l&m---------------------------------------------"));
        return msgs;
    }

}
