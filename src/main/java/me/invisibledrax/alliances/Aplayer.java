package me.invisibledrax.alliances;

import me.invisibledrax.alliances.alliances.Alliance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Aplayer {
	
	private static Main pl = Main.getInstance();
	private static HashMap<OfflinePlayer, Aplayer> players = new HashMap<>();
	
	private OfflinePlayer player;
	private boolean pvping;
	private int pvpCooldown = 0;
	private ArrayList<Alliance> allianceInvites = new ArrayList<>();
	
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

	public Alliance getTruce() {
		return Alliance.getAlliance(player);
	}

	public ArrayList<Alliance> getTruceInvites() {
		return allianceInvites;
	}

	public ArrayList<BukkitTask> inviteRunnables = new ArrayList<>();

	public void addTruceInvite(Alliance alliance) {
		allianceInvites.add(alliance);
		if (player.isOnline()) {
			Bukkit.getPlayer(player.getUniqueId()).sendMessage(ChatColor.YELLOW + "You have been invited to the " + alliance.getName() + " truce!");
		}
		BukkitTask run = new BukkitRunnable() {
			@Override
			public void run() {
				allianceInvites.remove(alliance);
				if (player.isOnline()) {
					Bukkit.getPlayer(player.getUniqueId()).sendMessage(ChatColor.YELLOW + "Your invite from the " + alliance.getName() + " truce has expired!");
				}
			}
		}.runTaskLater(pl, 20 * 120);
		inviteRunnables.add(run);
	}

	public void removeTruceInvite(Alliance alliance) {
		allianceInvites.remove(alliance);
	}

	public boolean hasTruceInvite(Alliance alliance) {
		if (allianceInvites.contains(alliance)) {
			return true;
		}
		return false;
	}
	
}
