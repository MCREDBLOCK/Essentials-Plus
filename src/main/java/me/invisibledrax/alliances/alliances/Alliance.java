package me.invisibledrax.alliances.alliances;

import me.invisibledrax.alliances.Aplayer;
import me.invisibledrax.alliances.Main;
import me.invisibledrax.alliances.util.SaveReloadConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Alliance {

    private static Main pl = Main.getInstance();
    private static HashMap<String, Alliance> allianceNames = new HashMap<>();
    private static HashMap<Aplayer, Alliance> playerAlliances = new HashMap<>();
    public static final int MAX_MEMBERS = 5;
    public static final String DEFAULT_DESCRIPTION = "Default :(";

    private String name;
    private File f;
    private OfflinePlayer leader;
    private String desc;
    private ArrayList<UUID> members = new ArrayList<>();

    private Alliance(String name, OfflinePlayer leader) {
        this.name = name;
        this.leader = leader;
    }

    private Alliance(File loadedFile) {
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

    public static Alliance createAlliance(String name, Player owner) {
        Alliance alliance = new Alliance(name, owner);
        allianceNames.put(name, alliance);
        playerAlliances.put(Aplayer.getAplayer(owner), alliance);

        alliance.getConfigFile().getParentFile().mkdirs();
        try {
            alliance.getConfigFile().createNewFile();
        } catch (IOException e) {
            // Don't print stack trace
        }
        alliance.members = new ArrayList<>();
        alliance.setName(name);
        alliance.setDescription(DEFAULT_DESCRIPTION);
        alliance.addMember(owner);
        alliance.setRole(owner, AllianceRole.Leader);
        return alliance;
    }

    public void disbandAlliance() {
        allianceNames.remove(name);
        for(OfflinePlayer off : getMembers()) {
            playerAlliances.remove(Aplayer.getAplayer(off));
        }
        getConfigFile().delete();
    }

    public static Alliance loadAlliance(File f) {
        Alliance alliance = new Alliance(f);
        allianceNames.put(alliance.getName(), alliance);
        return alliance;
    }

    public static ArrayList<Alliance> getAllAlliances() {
        ArrayList<Alliance> ar = new ArrayList<>();
        ar.addAll(allianceNames.values());
        return ar;
    }

    public static Alliance getAlliance(String name) {
        return allianceNames.get(name);
    }

    public static Alliance getAlliance(OfflinePlayer p) {
        return playerAlliances.get(Aplayer.getAplayer(p));
    }

    public static boolean exists(String name) {
        if (allianceNames.containsKey(name)) {
            return true;
        }
        return false;
    }

    public static boolean isInAlliance(OfflinePlayer player) {
        if (playerAlliances.containsKey(Aplayer.getAplayer(player))) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void registerAlliances() {
        if (!getConfigPath().exists()) {
            getConfigPath().mkdirs();
        }
        for (File f: getConfigPath().listFiles()) {
            Alliance alliance = loadAlliance(f);
            for (OfflinePlayer op : alliance.getMembers()) {
                playerAlliances.put(Aplayer.getAplayer(op), alliance);
                allianceNames.put(alliance.getName(), alliance);
                YamlConfiguration config = alliance.getConfig();
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

    public void setDescription(String desc) {
        this.desc = desc;
        YamlConfiguration config = getConfig();
        config.set("Description", desc);
        SaveReloadConfig.saveAndReload(config, getConfigFile());
    }

    public ArrayList<OfflinePlayer> getMembers() {
        ArrayList<OfflinePlayer> members = new ArrayList<>();
        for (UUID id : this.members) {
            members.add(Bukkit.getOfflinePlayer(id));
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

    public void addMember(OfflinePlayer p) {
        members.add(p.getUniqueId());
        playerAlliances.put(Aplayer.getAplayer(p), this);
        YamlConfiguration config = getConfig();
        config.set("Members." + p.getUniqueId() + ".Name", p.getName());
        config.set("Members." + p.getUniqueId() + ".Role", AllianceRole.Member.toString());
        SaveReloadConfig.saveAndReload(config, getConfigFile());
    }

    public void setRole(OfflinePlayer p, AllianceRole role) {
        YamlConfiguration config = getConfig();
        config.set("Members." + p.getUniqueId() + ".Role", role.toString());
        SaveReloadConfig.saveAndReload(config, getConfigFile());
    }

    public void removeMember(OfflinePlayer p) {
        members.remove(p.getUniqueId());
        playerAlliances.remove(Aplayer.getAplayer(p));
        YamlConfiguration config = getConfig();
        config.set("Members." + p.getUniqueId(), null);
        SaveReloadConfig.saveAndReload(config, getConfigFile());
    }

    public String getName() {
        if (name == null) {
            name = getConfig().getString("Name");
        }
        return name;
    }

    public void setName(String name) {
        allianceNames.put(name, this);
        this.name = name;
        YamlConfiguration config = getConfig();
        config.set("Name", name);
        SaveReloadConfig.saveAndReload(config, getConfigFile());
    }

    public void changeName(String name) {
        Alliance alliance = new Alliance(name, leader);
        alliance.setName(name);
        alliance.setDescription(getDescription());
        for (OfflinePlayer member : getMembers()) {
            alliance.addMember(member);
            alliance.setRole(member, getRole(member));
            playerAlliances.put(Aplayer.getAplayer(member), alliance);
        }
        allianceNames.remove(getName());
        allianceNames.put(name, alliance);
        getConfigFile().delete();
        alliance.getConfigFile();
        getConfigFile().delete();
    }

    public AllianceRole getRole(OfflinePlayer p) {
        return AllianceRole.fromString(getConfig().getString("Members." + p.getUniqueId() + ".Role"));
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
        ar.add(ChatColor.translateAlternateColorCodes('&', "&e&l&m---------------&r &e&l" + getName() + "&r &e&l&m----------------"));
        ar.add("");
        ar.add(ChatColor.translateAlternateColorCodes('&', "Description: &e" + getDescription()));
        String members = "Members: ";
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
        ar.add("");
        ar.add(ChatColor.translateAlternateColorCodes('&', "&e&l&m--------------------------------------"));
        return ar;
    }

}
