package com.redblock6.mccore.utils;

import me.invisibledrax.alliances.Main;
import me.invisibledrax.alliances.truces.Nations;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

import static me.invisibledrax.alliances.Main.translate;

public class Aplayer {
	
	private static Main pl = Main.getInstance();
	private static HashMap<OfflinePlayer, Aplayer> players = new HashMap<>();
	
	private OfflinePlayer player;
	private boolean isTeleporting;
	private boolean pvping;
	private int pvpCooldown = 0;
	private int tpDelay;
	private ArrayList<Nations> nationsInvites = new ArrayList<>();
	
	private Aplayer(OfflinePlayer player) {
		this.player = player;
	}
	
	public static Aplayer getAplayer(OfflinePlayer p) {
		if(players.containsKey(p)) {
			return players.get(p);
		}
		else {
			Aplayer ap = new Aplayer(p);
			players.put(p, ap);
			return ap;
		}
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}


	public boolean isPvping() {
		return pvping;
	}

	public void setPvping(boolean pvping) {
		this.pvping = pvping;
	}
	
	public void startPvpCountdown() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (pvpCooldown == 0) {
					setPvping(false);
					getPlayer().getPlayer().sendMessage(ChatColor.RED + "Exited PVP");
					cancel();
				}
				else {
					pvpCooldown--;
				}
			}
		}.runTaskTimer(pl, 0, 20);
	}

	public int getTpDelay() {
		return tpDelay;
	}

	public void setTpDelay(int tpDelay) {
		this.tpDelay = tpDelay;
	}

	public Nations getTruce() {
		return Nations.getTruce(player);
	}

	public ArrayList<Nations> getTruceInvites() {
		return nationsInvites;
	}

	public ArrayList<BukkitTask> inviteRunnables = new ArrayList<>();

	public void addTruceInvite(Nations nations) {
		Player p = Bukkit.getPlayer(player.getUniqueId());
		nationsInvites.add(nations);
		if (player.isOnline()) {
			p.sendMessage(translate("&2&l> &fYou've been invited to the" + nations.getName() + "&f! &2&lACCEPT  &4&lDENY"));
		}
		BukkitTask run = new BukkitRunnable() {
			@Override
			public void run() {
				nationsInvites.remove(nations);
				if (player.isOnline()) {
					p.sendMessage(translate("&2&l> &fYour invite from the " + nations.getName() + "&f! &2&lACCEPT  &4&lDENY"));
				}
			}
		}.runTaskLater(pl, 20 * 120);
		inviteRunnables.add(run);
	}

	public void removeTruceInvite(Nations nations) {
		nationsInvites.remove(nations);
	}

	public boolean hasTruceInvite(Nations nations) {
		if (nationsInvites.contains(nations)) {
			return true;
		}
		return false;
	}
	
}
