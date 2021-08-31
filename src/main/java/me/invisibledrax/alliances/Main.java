package me.invisibledrax.alliances;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import static me.invisibledrax.alliances.Register.registerCommands;
import static me.invisibledrax.alliances.Register.registerEvents;

public final class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        registerEvents();
        registerCommands();
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

    public static Main getInstance() {
        return instance;
    }

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
