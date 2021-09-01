package com.redblock6.mccore.utils;

import me.invisibledrax.alliances.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataFromConfig {

	private static Main pl = Main.getInstance();

	public static ItemStack itemFromConfig(YamlConfiguration config, String path) {
		Material mat = Material.getMaterial(config.getString(path + ".Type"));
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		String name = ChatColor.translateAlternateColorCodes('&', config.getString(path + ".Name"));
		meta.setDisplayName(name);
		List<String> configLore = config.getStringList(path + ".Lore");
		List<String> lore = new ArrayList<>();
		for (String s : configLore) {
			lore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		boolean enchanted = config.getBoolean(path + ".Enchanted");
		if (enchanted) {
			meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, enchanted);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		item.setItemMeta(meta);
		return item;
	}

	public static void itemToConfig(ItemStack item, File file, YamlConfiguration config, String path) {
		config.set(path + ".Type", item.getType().toString());
		ItemMeta meta = item.getItemMeta();
		config.set(path + ".Name", meta.getDisplayName());
		List<String> configLore = new ArrayList<>();
		for (String s : meta.getLore()) {
			configLore.add(s);
		}
		config.set(path + ".Lore", configLore);
		// Check if the item has any enchantments
		if (!(item.getEnchantments().size() < 1)) {
			config.set(path + ".Enchanted", true);
		} else {
			config.set(path + ".Enchanted", false);
		}
		SaveReloadConfig.saveAndReload(config, file);
	}
	
	public static Location locationFromConfig(YamlConfiguration config, String path) {
		World world = pl.getMultiverseWorld(config.getString(path + ".World"));
		double x = config.getDouble(path + ".X");
		double y = config.getDouble(path + ".Y");
		double z = config.getDouble(path + ".Z");
		float yaw = (float) config.getDouble(path + ".Yaw");
		float pitch = (float) config.getDouble(path + ".Pitch");
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	public static void locationToConfig(Location loc, File file, YamlConfiguration config, String path) {
		config.set(path + ".World", loc.getWorld().getName());
		config.set(path + ".X", loc.getX());
		config.set(path + ".Y", loc.getY());
		config.set(path + ".Z", loc.getZ());
		config.set(path + ".Yaw", loc.getYaw());
		config.set(path + ".Pitch", loc.getPitch());
		SaveReloadConfig.saveAndReload(config, file);
	}

}
