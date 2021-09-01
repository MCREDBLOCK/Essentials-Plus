package me.invisibledrax.alliances;

import me.invisibledrax.alliances.alliances.FriendlyFireListener;
import me.invisibledrax.alliances.alliances.PlayerChatListener;
import me.invisibledrax.alliances.alliances.Alliance;
import me.invisibledrax.alliances.alliances.AllianceCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Register {
    private static Main pl = Main.getInstance();
    private static PluginManager pm = Bukkit.getPluginManager();

    public static void registerEvents() {
        // Register events
        pm.registerEvents(new PlayerChatListener(), pl);
        pm.registerEvents(new FriendlyFireListener(), pl);
    }

    public static void registerCommands() {
        // Register commands
        pl.getCommand("alliance").setExecutor(new AllianceCommand());
        pl.getCommand("alliance").setName(Main.commandName);
    }

    public static void registerMisc() {
        Alliance.registerAlliances();
    }

}
