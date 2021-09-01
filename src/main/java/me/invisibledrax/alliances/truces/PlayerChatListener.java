package me.invisibledrax.alliances.truces;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onMessageSend(AsyncPlayerChatEvent e) {
        if (Nations.isInTruce(e.getPlayer())) {
            e.setFormat(ChatColor.YELLOW + "" + ChatColor.BOLD + Nations.getTruce(e.getPlayer()).getName()
                    + " " + e.getPlayer().getDisplayName() + ChatColor.DARK_AQUA + ">> " + ChatColor.WHITE + e.getMessage());
        }
    }

}
