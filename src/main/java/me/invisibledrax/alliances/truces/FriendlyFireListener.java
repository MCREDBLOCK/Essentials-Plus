package me.invisibledrax.alliances.truces;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static me.invisibledrax.alliances.Main.translate;

public class FriendlyFireListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Player)) {
            Player hurt = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            if (Nations.getTruce(damager).equals(Nations.getTruce(hurt))) {
                damager.sendMessage(translate("&4&l> &fYou can't hurt members of your nation!"));
                e.setCancelled(true);
            }
        }
    }

}
