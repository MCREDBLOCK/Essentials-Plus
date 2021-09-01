package me.invisibledrax.alliances.truces;

import com.redblock6.mccore.utils.Aplayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
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

import static me.invisibledrax.alliances.Main.translate;

public class NationCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nation")) {
            if (args.length == 0) {
                for (String msg : getHelpMessage()) {
                    sender.sendMessage(msg);
                }
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(translate("&4&l> &fOnly players can create nations!"));
                return true;
            }
            Player p = (Player) sender;
            // Create truce
            if (args[0].equalsIgnoreCase("create")) {
                // Check if player is already in a truce
                if (Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> &fYou are already in a nation!"));
                    return true;
                }
                // Check if player set a valid name
                if (args.length < 2 || args[1].length() < 3 || args[1].length() > 16) {
                    p.sendMessage(translate("&4&l> &fYou must set a name between 3 and 12 characters long!"));
                    return true;
                }
                if (Nations.exists(args[1])) {
                    p.sendMessage(translate("&4&l> &fA nation with this name already exists!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Nations t = Nations.createTruce(args[1], p);
            }
            if (args[0].equalsIgnoreCase("disband")) {
                // Check if player is in a truce
                if (!Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> &fYou aren't in a nation!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Nations nations = Nations.getTruce(p);
                // Check if player has permission to set truce description
                if (!nations.getRole(p).equals(NationRole.Leader)) {
                    p.sendMessage(translate("&4&l> &fYou must be the leader to disband the nation!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                for (OfflinePlayer off : nations.getMembers()) {
                    if (off.isOnline()) {
                        Player on = Bukkit.getPlayer(off.getName());
                        if (on.equals(p)) {
                            continue;
                        }
                        on.sendMessage(translate("&4&l> &fYour nation was disbanded!"));
                        on.sendTitle(translate("&4&l✖"), translate("&fYour nation was disbanded!"));
                        on.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
                    }
                }
                p.sendMessage(translate("&4&l> &fYou disbanded the &c" + nations.getName() + "&f!"));
                p.sendTitle(translate("&4&l✖"), translate("&fYou disbanded the &c" + nations.getName() + "&f!"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
                nations.disbandTruce();

            }
            if (args[0].equalsIgnoreCase("info")) {
                if (args.length == 1) {
                    if (!Nations.isInTruce(p)) {
                        p.sendMessage(translate("&4&l> &fPlease specify a nation!"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                        return true;
                    }
                    // Send info of players own truce
                    Nations nations = Nations.getTruce(p);
                    ArrayList<String> msgs = nations.getInfoMessage();
                    for (String s : msgs) {
                        p.sendMessage(s);
                    }
                }
                else {
                    // Send info of specified truce
                    if (!Nations.exists(args[1])) {
                        p.sendMessage(translate("&4&l> &fThat nation doesn't exist!"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                        return true;
                    }
                    Nations nations = Nations.getTruce(args[1]);
                    ArrayList<String> msgs = nations.getInfoMessage();
                    for (String s : msgs) {
                        p.sendMessage(s);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("who")) {
                if (args.length < 2) {
                    p.sendMessage(translate("&4&l> &fPlease specify whose nation you want to see!"));
                    return true;
                }
                List<OfflinePlayer> offs = new ArrayList<>();
                for (OfflinePlayer off : Bukkit.getOfflinePlayers()) {
                    offs.add(off);
                }
                if (!offs.contains(Bukkit.getOfflinePlayer(args[1]))) {
                    p.sendMessage(translate( "&4&l> &fCannot find player or that player does not exist!"));
                    return true;
                }
                OfflinePlayer off = Bukkit.getOfflinePlayer(args[1]);
                if (!Nations.isInTruce(off)) {
                    p.sendMessage(translate("&4&l> &c" + off + " &fis not in a nation"));
                    return true;
                }
                Nations nations = Nations.getTruce(off);
                ArrayList<String> msgs = nations.getInfoMessage();
                for(String msg : msgs) {
                    p.sendMessage(msg);
                }
            }
            if (args[0].equalsIgnoreCase("desc")) {
                // Check if player is in a truce
                if (!Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> &fYou must be in a nation to send that command!"));
                    return true;
                }
                Nations nations = Nations.getTruce(p);
                // Check if player has permission to set truce description
                if (!nations.getRole(p).equals(NationRole.Leader) && !nations.getRole(p).equals(NationRole.CoLeader)) {
                    p.sendMessage(translate("&4&l> &fYou must be a nation &c&lCO LEADER &for above to use this command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
                    nations.setDescription(p, Nations.DEFAULT_DESCRIPTION, true);
                    p.sendMessage(translate("&2&l> &fSuccessfully reset nation description!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
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
                    p.sendMessage(translate("&4&l> &fThe description must be between 1 and 50 characters!"));
                    return true;
                }
                nations.setDescription(p, description, true);
                p.sendMessage(translate("&2&l> &fSuccessfully set nation description!"));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
            }
            if (args[0].equalsIgnoreCase("invite")) {
                // Check if player is in a truce
                if (!Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> You must be in a nation to send that command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Nations nations = Nations.getTruce(p);
                // Check if player has permission to invite players
                if (!nations.getRole(p).equals(NationRole.Leader) && !nations.getRole(p).equals(NationRole.CoLeader)) {
                    p.sendMessage(translate("&4&l> &fYou must be a nation &c&lCO LEADER &for above to use this command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (nations.getMembers().size() >= Nations.MAX_MEMBERS) {
                    p.sendMessage(translate("&4&l> &fYou can't invite any more members to your nation!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(translate( "&4&l> &fPlease specify who you would like to invite!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
                    p.sendMessage(translate("&4&l> &fCannot find player or player does not exist!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Player invitee = Bukkit.getPlayer(args[1]);
                if (Nations.isInTruce(invitee)) {
                    p.sendMessage(translate("&4&l> &fThis player is already in a nation!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Aplayer.getAplayer(invitee).addTruceInvite(nations);
                p.sendMessage(translate("&2&l> &fSuccessfully invited &a" + invitee.getName() + " &fto the nation!"));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
            }
            if (args[0].equalsIgnoreCase("join")) {
                if (Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> &fYou are already in a nation!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(translate("&4&l> &fPlease specify the nation you would like to join!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (!Nations.exists(args[1])) {
                    p.sendMessage(translate("&4&l> &fThis nation does not exist!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Nations nations = Nations.getTruce(args[1]);
                Aplayer ap = Aplayer.getAplayer(p);
                if (!ap.hasTruceInvite(nations)) {
                    p.sendMessage(translate("&4&l> &fYou don't have any pending invites from &c" + nations.getName()));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                for (BukkitTask task : ap.inviteRunnables) {
                    task.cancel();
                }
                ap.getTruceInvites().clear();
                for (Player member : nations.getOnlineMembers()) {
                    Bukkit.getPlayer(member.getUniqueId()).sendMessage(translate("&2&l> &a" + p.getName() + " &fjoined " + nations.getName() + "!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 2);
                }
                nations.addMember(p, true);
                p.sendMessage(translate("&2&l> &fSuccessfully joined the &a" + nations.getName()));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
            }
            if (args[0].equalsIgnoreCase("promote")) {
                // Check if player is in a truce
                if (!Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> &fYou must be in a nation to send that command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Nations nations = Nations.getTruce(p);
                // Check if player has permission to set truce description
                if (!nations.getRole(p).equals(NationRole.Leader)) {
                    p.sendMessage(translate("&4&l> &fYou must be nation &4&lLEADER to use this command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(translate("&4&l> &fPlease specify who you want to promote to &c&lCO LEADER&f!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (!nations.getMembers().contains(Bukkit.getOfflinePlayer(args[1]))) {
                    p.sendMessage(translate("&4&l> &fThis player is not in your nation!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                OfflinePlayer off = Bukkit.getOfflinePlayer(args[1]);
                if (off.equals(p)) {
                    p.sendMessage(translate("&4&l> &fYou can't promote yourself, dingus."));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (nations.getRole(off).equals(NationRole.CoLeader)) {
                    p.sendMessage(translate("&4&l> &fThis member is already a &c&lCO LEADER&f!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                nations.setRole(off, NationRole.CoLeader);
                for (Player member : nations.getOnlineMembers()) {
                    if (member.equals(p)) {
                        continue;
                    }
                    if (member.equals(off)) {
                        member.sendMessage(translate("&2&l> &fYou have been promoted to &a&lCO LEADER&f!"));
                        member.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                        continue;
                    }
                    member.sendMessage(translate("&2&l> &a" + off.getName() + " &fhas been promoted to &a&lCO LEADER&f!"));
                    member.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 1);
                }
                p.sendMessage(translate("&2&l> &fSuccessfully promoted &a" + off.getName() + " &fto &a&lCO LEADER&f!"));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (!Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> &fYou are not in a nation!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Nations nations = Nations.getTruce(p);
                nations.removeMember(p);
                p.sendMessage(translate("&2&l> &fYou have left the &a" + nations.getName()));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
                for (Player member : nations.getOnlineMembers()) {
                    member.sendMessage(translate("&4&l> &f" + p.getName() + " left the nation!"));
                    member.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 1);
                }
            }
            if (args[0].equalsIgnoreCase("kick")) {
                // Check if player is in a truce
                if (!Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> &fYou must be in a nation to send that command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Nations nations = Nations.getTruce(p);
                // Check if player has permission to set truce description
                if (!nations.getRole(p).equals(NationRole.Leader) && !nations.getRole(p).equals(NationRole.CoLeader)) {
                    p.sendMessage(translate("&4&l> &fYou must be a nation &c&lCO LEADER+ &fto send that command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(translate("&4&l> &fPlease specify who you would like to kick!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (!nations.getMembers().contains(Bukkit.getPlayer(args[1]))) {
                    p.sendMessage(translate("&4&l> &f" + Bukkit.getPlayer(args[1]).getName() + "is not in your nation!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                OfflinePlayer member = Bukkit.getOfflinePlayer(args[1]);
                if (member.equals(p)) {
                    p.sendMessage(translate("&4&l> &fYou can't kick yourself from the nation, dingus."));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (nations.getRole(p).equals(NationRole.CoLeader) && nations.getRole(p).equals(NationRole.CoLeader)) {
                    p.sendMessage(translate("&4&l> &fYou must be the nation &4&lLEADER to kick the &c&lCO LEADER&f!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (member.equals(nations.getLeader())) {
                    p.sendMessage(translate("&4&l> &fYou can't kick the nation &4&lLEADER&f!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                for (Player on : nations.getOnlineMembers()) {
                    String kickerName = "&4&lLEADER &f" + p.getName();
                    if (nations.getRole(p).equals(NationRole.CoLeader)) {
                        kickerName = "&c&lCO LEADER &f" + p.getName();
                    }
                    if (on.equals(p)) {
                        on.sendMessage(translate("&2&l> &fSuccessfully kicked &a" + member.getName() + " &ffrom the nation!"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
                        continue;
                    }
                    if (on.equals(member)) {
                        on.sendMessage(translate("&4&l> " + kickerName + " has kicked you from " + nations.getName() + "!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_BAT_DEATH, 100, 1);
                        continue;
                    }
                    on.sendMessage(translate("&4&l> " + kickerName + " has kicked " + member.getName() + " from the truce!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_BAT_DEATH, 100, 1);
                }
                nations.removeMember(member);
            }
            if (args[0].equalsIgnoreCase("rename")) {
                // Check if player is in a truce
                if (!Nations.isInTruce(p)) {
                    p.sendMessage(translate("&4&l> &fYou must be in a nation to send that command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                Nations nations = Nations.getTruce(p);
                // Check if player has permission to set truce name
                if (!nations.getRole(p).equals(NationRole.Leader)) {
                    p.sendMessage(translate("&4&l> &fYou must be the nation &4&lLEADER to send that command!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(translate("&4&l> &fPlease specify a new name!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                if (Nations.exists(args[1])) {
                    p.sendMessage(translate("&4&l> &fA nation with this name already exists!"));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1);
                    return true;
                }
                nations.changeName(args[1], nations.getName());
                p.sendMessage(translate("&2&l> &fSuccessfully changed nation name!"));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
                for (Player member : nations.getOnlineMembers()) {
                    if (member.equals(p)) {
                        continue;
                    }
                    member.sendMessage("&2&l> &fYour nation name has been changed to &a" + args[1] + "&f!");
                    member.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 1);
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
        if (command.getName().equalsIgnoreCase("nation")) {
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
                        for (Nations nations : Nations.getAllTruces()) {
                            names.add(nations.getName());
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
                        for(Nations t : ap.getTruceInvites()) {
                            names.add(t.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("promote")) {
                        ArrayList<String> names = new ArrayList<>();
                        for(OfflinePlayer off : Nations.getTruce(p).getMembers()) {
                            names.add(off.getName());
                        }
                        StringUtil.copyPartialMatches(args[1], names, completions);
                    }
                    if (args[0].equalsIgnoreCase("kick")) {
                        ArrayList<String> names = new ArrayList<>();
                        Nations nations = Nations.getTruce(p);
                        for(Player member : nations.getOnlineMembers()) {
                            names.add(member.getName());
                            if (member.equals(nations.getLeader())) {
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
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&m---------------------------------"));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&lNATIONS"));
        msgs.add(ChatColor.translateAlternateColorCodes('&', ""));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation create: &cCreates a nation that you own with the specified name."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation disband: &cDisbands your nation if you are the leader, kicking all the members."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation info: &cShows the info regarding the specified nation."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation who: &cShows the nation info of the specified player."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation desc: &cChanges the description of your nation."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation invite: &cInvites specified player to your nation. Invites expire after 2 minutes."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation join: &cJoins the specified nation if you are not already in one."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation promote: &cPromotes a regular member of the nation to an officer."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation rename: &cRenames the nation with the specified name."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation leave: &cLeaves your current nation."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&l/nation kick: &cKicks the specified player from your nation."));
        msgs.add(ChatColor.translateAlternateColorCodes('&', "&4&m---------------------------------"));
        return msgs;
    }

}
