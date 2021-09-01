package me.invisibledrax.alliances;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static me.invisibledrax.alliances.Register.*;

public final class Main extends JavaPlugin {
    private static Main instance;
    public static String commandName;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        if (!getDataFolder().exists()) {
            setupConfig();
        }
        commandName = getConfig().getString("commandName");
        registerEvents();
        registerCommands();
        registerMisc();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupConfig() {
        //Main
        FileConfiguration config = getConfig();
        config.set("commandName", "alliance");
        saveConfig();
        //Blacklisted words
        File blacklistFile = new File(getDataFolder(), "blacklisted words.yml");
        FileConfiguration blacklistConfig = new YamlConfiguration();
        try {
            blacklistConfig.load(blacklistFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        ArrayList<String> blacklist = new ArrayList<>();
        blacklist.add("shit");
        blacklist.add("fuck");
        blacklist.add("dick");
        blacklist.add("cock");
        blacklist.add("ass");
        config.set("words", blacklist);
        try {
            blacklistConfig.save(blacklistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static World getMultiverseWorld(String name) {
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().equals(name)) {
                return world;
            }
        }
        return null;
    }

    public static Main getInstance() {
        return instance;
    }

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
