package me.invisibledrax.alliances;

import com.redblock6.mccore.commands.Gamemode;
import com.redblock6.mccore.commands.StreamingCommand;
import com.redblock6.mccore.commands.WarnReboot;
import com.redblock6.mccore.events.InteractEvent;
import com.redblock6.mccore.events.JoinLeaveEvent;
import me.invisibledrax.alliances.truces.FriendlyFireListener;
import me.invisibledrax.alliances.truces.PlayerChatListener;
import me.invisibledrax.alliances.truces.Nations;
import me.invisibledrax.alliances.truces.NationCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Register {
    private static Main pl = Main.getInstance();
    private static PluginManager pm = Bukkit.getPluginManager();

    public static void registerEvents() {
        // Register events
        pm.registerEvents(new PlayerChatListener(), pl);
        pm.registerEvents(new FriendlyFireListener(), pl);
        pm.registerEvents(new InteractEvent(), pl);
        pm.registerEvents(new JoinLeaveEvent(), pl);
    }

    public static void registerCommands() {
        // Register commands
        pl.getCommand("nation").setExecutor(new NationCommand());
        pl.getCommand("warnreboot").setExecutor(new WarnReboot());
        pl.getCommand("streaming").setExecutor(new StreamingCommand());
        pl.getCommand("gmc").setExecutor(new Gamemode());
        pl.getCommand("gms").setExecutor(new Gamemode());
        pl.getCommand("gma").setExecutor(new Gamemode());
        pl.getCommand("gmsp").setExecutor(new Gamemode());
    }

    public static void registerMisc() {
        Nations.registerTruces();
    }

}
