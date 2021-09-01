package com.redblock6.mccore.utils;

import org.bukkit.Server;

import static org.bukkit.Bukkit.getServer;

public class TimeChecker {
    public static boolean day() {
        Server server = getServer();
        long time = server.getWorld("world").getTime();

        return time < 12300 || time > 23850;
    }
}
