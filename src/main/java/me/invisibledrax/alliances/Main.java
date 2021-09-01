package me.invisibledrax.alliances;

import com.redblock6.mccore.bot.BotMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import static me.invisibledrax.alliances.Register.*;

public final class Main extends JavaPlugin {
    private static Main instance;
    public static boolean streaming;
    public static BotMain bot;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        registerEvents();
        registerCommands();
        registerMisc();
        loadConfigs();
        streaming = true;
        bot = new BotMain(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static World getMultiverseWorld(String name) {
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().equals(name)) {
                return world;
            }
        }
        return null;
    }

    public void loadConfigs() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public static Main getInstance() {
        return instance;
    }
    public static BotMain getBot() {
        return bot;
    }
    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
