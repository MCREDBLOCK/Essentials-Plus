package me.invisibledrax.alliances.alliances;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FriendlyFireListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Player)) {
            Player hurt = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            if (Alliance.getAlliance(damager).equals(Alliance.getAlliance(hurt))) {
                damager.sendMessage(ChatColor.RED + "(!) You cannot hurt members of your truce!");
                e.setCancelled(true);
            }
        }
    }

}