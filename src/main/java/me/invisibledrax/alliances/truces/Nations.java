package me.invisibledrax.alliances.truces;

import me.invisibledrax.alliances.Main;
import com.redblock6.mccore.utils.Aplayer;
import com.redblock6.mccore.utils.SaveReloadConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import static me.invisibledrax.alliances.Main.getBot;
import static me.invisibledrax.alliances.Main.translate;

public class Nations {

    private static Main pl = Main.getInstance();
    private static HashMap<String, Nations> truceNames = new HashMap<>();
    private static HashMap<Aplayer, Nations> playerTruces = new HashMap<>();
    public static final int MAX_MEMBERS = 11;
    public static final String DEFAULT_DESCRIPTION = "A RedSMP Nation";

    private String name;
    private File f;
    private OfflinePlayer leader;
    private String desc;
    private ArrayList<UUID> members = new ArrayList<>();

    private Nations(String name, OfflinePlayer leader) {
        this.name = name;
        this.leader = leader;
    }

    private Nations(File loadedFile) {
        f = loadedFile;
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(loadedFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        name = config.getString("Name");
        desc = config.getString("Description");
        ArrayList<UUID> members = new ArrayList<>();
        for (String s: config.getConfigurationSection("Members").getKeys(false)) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(s));
            members.add(op.getUniqueId());
            if (config.get("Members." + s + ".Role").equals("Leader")) {
                leader = op;
            }
        }
        this.members = members;
    }

    public static Nations createTruce(String name, Player owner) {
        Nations nations = new Nations(name, owner);
        truceNames.put(name, nations);
        playerTruces.put(Aplayer.getAplayer(owner), nations);

        nations.getConfigFile().getParentFile().mkdirs();
        try {
            nations.getConfigFile().createNewFile();
        } catch (IOException e) {
            // Don't print stack trace
        }
        nations.members = new ArrayList<>();
        nations.setName(name);
        nations.setDescription(owner, DEFAULT_DESCRIPTION, false);
        nations.addMember(owner, false);
        nations.setRole(owner, NationRole.Leader);
        owner.sendMessage(translate("&2&l> &fSuccessfully created &a" + name + "&f!"));
        owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 100, 2);
        owner.playSound(owner.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 2);
        getBot().sendNationsAnnouncement(owner.getName(), name, NationReason.CREATED);
        return nations;
    }

    public void disbandTruce() {
        getBot().sendNationsAnnouncement(getLeader().getName(), name, NationReason.DISBANDED);
        truceNames.remove(name);
        for(OfflinePlayer off : getMembers()) {
            playerTruces.remove(Aplayer.getAplayer(off));
        }
        getConfigFile().delete();
    }

    public static Nations loadTruce(File f) {
        Nations nations = new Nations(f);
        truceNames.put(nations.getName(), nations);
        return nations;
    }

    public static ArrayList<Nations> getAllTruces() {
        ArrayList<Nations> ar = new ArrayList<>();
        ar.addAll(truceNames.values());
        return ar;
    }

    public static Nations getTruce(String name) {
        return truceNames.get(name);
    }

    public static Nations getTruce(OfflinePlayer p) {
        return playerTruces.get(Aplayer.getAplayer(p));
    }

    public static boolean exists(String name) {
        if (truceNames.containsKey(name)) {
            return true;
        }
        return false;
    }

    public static boolean isInTruce(OfflinePlayer player) {
        if (playerTruces.containsKey(Aplayer.getAplayer(player))) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void registerTruces() {
        if (!getConfigPath().exists()) {
            getConfigPath().mkdirs();
        }
        for (File f: getConfigPath().listFiles()) {
            Nations nations = loadTruce(f);
            for (OfflinePlayer op : nations.getMembers()) {
                playerTruces.put(Aplayer.getAplayer(op), nations);
                truceNames.put(nations.getName(), nations);
                YamlConfiguration config = nations.getConfig();
                config.set("Members." + op.getUniqueId() + ".Name", op.getName());
            }
        }
    }

    public OfflinePlayer getLeader() {
        return leader;
    }

    public String getDescription() {
        if (desc == null) {
            desc = getConfig().getString("Description");
        }
        return desc;
    }

    public void setDescription(Player leader, String desc, boolean flag) {
        this.desc = desc;
        YamlConfiguration config = getConfig();
        config.set("Description", desc);
        SaveReloadConfig.saveAndReload(config, getConfigFile());
        if (flag) {
            getBot().sendNationsAnnouncement(leader.getName(), Nations.getTruce(leader).getName(), NationReason.CHANGED_DESC, desc);
        }
    }

    public ArrayList<OfflinePlayer> getMembers() {
        ArrayList<OfflinePlayer> members = new ArrayList<>();
        for (UUID id : this.members) {
            members.add(Bukkit.getOfflinePlayer(id));
        }
        return members;
    }

    public ArrayList<String> getMemberNames() {
        ArrayList<String> members = new ArrayList<>();
        for (UUID id : this.members) {
            members.add(Bukkit.getOfflinePlayer(id).getName());
        }
        return members;
    }

    public ArrayList<Player> getOnlineMembers() {
        ArrayList<Player> members = new ArrayList<>();
        for (OfflinePlayer off : getMembers()) {
            if (off.isOnline()) {
                members.add(Bukkit.getPlayer(off.getUniqueId()));
            }
        }
        return members;
    }

    public void addMember(OfflinePlayer p, boolean flag) {
        members.add(p.getUniqueId());
        playerTruces.put(Aplayer.getAplayer(p), this);
        YamlConfiguration config = getConfig();
        config.set("Members." + p.getUniqueId() + ".Name", p.getName());
        config.set("Members." + p.getUniqueId() + ".Role", NationRole.Member.toString());
        SaveReloadConfig.saveAndReload(config, getConfigFile());
        if (flag) {
            getBot().sendNationsAnnouncement(getLeader().getName(), name, NationReason.MEMBER_ADDED, p.getName());
        }
    }

    public void setRole(OfflinePlayer p, NationRole role) {
        YamlConfiguration config = getConfig();
        config.set("Members." + p.getUniqueId() + ".Role", role.toString());
        SaveReloadConfig.saveAndReload(config, getConfigFile());
    }

    public void removeMember(OfflinePlayer p) {
        members.remove(p.getUniqueId());
        playerTruces.remove(Aplayer.getAplayer(p));
        YamlConfiguration config = getConfig();
        config.set("Members." + p.getUniqueId(), null);
        SaveReloadConfig.saveAndReload(config, getConfigFile());
        getBot().sendNationsAnnouncement(getLeader().getName(), name, NationReason.MEMBER_KICKED, p.getName());
    }

    public String getName() {
        if (name == null) {
            name = getConfig().getString("Name");
        }
        return name;
    }

    public void setName(String name) {
        truceNames.put(name, this);
        this.name = name;
        YamlConfiguration config = getConfig();
        config.set("Name", name);
        SaveReloadConfig.saveAndReload(config, getConfigFile());
    }

    public void changeName(String name, String oldName) {
        Nations nations = new Nations(name, leader);
        getBot().sendNationsAnnouncement(leader.getName(), name, NationReason.CHANGED_NAME, oldName, name);
        nations.setName(name);
        nations.setDescription((Player) leader, getDescription(), false);
        for (OfflinePlayer member : getMembers()) {
            nations.addMember(member, false);
            nations.setRole(member, getRole(member));
            playerTruces.put(Aplayer.getAplayer(member), nations);
        }
        truceNames.remove(getName());
        truceNames.put(name, nations);
        getConfigFile().delete();
        nations.getConfigFile();
        getConfigFile().delete();

    }

    public NationRole getRole(OfflinePlayer p) {
        return NationRole.fromString(getConfig().getString("Members." + p.getUniqueId() + ".Role"));
    }

    public File getConfigFile() {
        if (f == null) {
            return new File(getConfigPath(), name + ".yml");
        }
        else {
            return f;
        }
    }

    public YamlConfiguration getConfig() {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(getConfigFile());
        } catch (IOException | InvalidConfigurationException e){

        }
        return config;
    }

    public static File getConfigPath() {
        return new File(pl.getDataFolder() + "/Truces");
    }

    public ArrayList<String> getInfoMessage() {
        ArrayList<String> ar = new ArrayList<>();
        ar.add(ChatColor.translateAlternateColorCodes('&', "&4&m---------------------------------"));
        ar.add(ChatColor.translateAlternateColorCodes('&', "&4&l" + getName().toUpperCase()));
        ar.add("");
        ar.add(ChatColor.translateAlternateColorCodes('&', "&f" + getDescription()));
        ar.add("");
        ar.add(ChatColor.translateAlternateColorCodes('&', "&4&lMEMBERS"));
        String members = "";
        ArrayList<Player> online = new ArrayList<>();
        ArrayList<OfflinePlayer> offline = new ArrayList<>();
        for (OfflinePlayer member : getMembers()) {
            if (member.isOnline()) {
                online.add(Bukkit.getPlayer(member.getUniqueId()));
            }
            else {
                offline.add(member);
            }
        }
        String onlineString = ChatColor.GREEN + "";
        String offlineString = ChatColor.RED + "";
        Iterator<Player> onlineIt = online.iterator();
        while(onlineIt.hasNext()) {
            Player onlineP = onlineIt.next();
            if (onlineIt.hasNext()) {
                onlineString += getRole(onlineP).getPrefix() + onlineP.getName() + ", ";
            }
            else {
                onlineString += getRole(onlineP).getPrefix() + onlineP.getName();
            }
        }
        Iterator<OfflinePlayer> offlineIt = offline.iterator();
        while(offlineIt.hasNext()) {
            OfflinePlayer offlineP = offlineIt.next();
            if (offlineP == offline.get(0)) {
                offlineString += ", ";
            }
            if (offlineIt.hasNext()) {
                offlineString += getRole(offlineP).getPrefix() + offlineP.getName() + ", ";
            }
            else {
                offlineString += getRole(offlineP).getPrefix() + offlineP.getName();
            }
        }
        members += onlineString + offlineString;
        ar.add(members);
        ar.add(ChatColor.translateAlternateColorCodes('&', "&4&m---------------------------------"));
        return ar;
    }

}
