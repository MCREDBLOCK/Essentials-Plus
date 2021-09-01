package me.invisibledrax.alliances.filter;

import me.invisibledrax.alliances.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BlacklistedWords {

    public static ArrayList<String> getBlacklistedWords() {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(new File(Main.getInstance().getDataFolder(), "blacklisted words.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return (ArrayList<String>) config.getList("words");
    }

    public static boolean allowed(String s) {
        for (String w : getBlacklistedWords()) {
            if (s.contains(w)) {
                return false;
            }
        }
        return true;
    }

}
