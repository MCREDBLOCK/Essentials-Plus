package me.invisibledrax.alliances;

import me.invisibledrax.alliances.truces.FriendlyFireListener;
import me.invisibledrax.alliances.truces.PlayerChatListener;
import me.invisibledrax.alliances.truces.Truce;
import me.invisibledrax.alliances.truces.TruceCommand;
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
        pl.getCommand("truce").setExecutor(new TruceCommand());
    }

    public static void registerMisc() {
        Truce.registerTruces();
    }

}
