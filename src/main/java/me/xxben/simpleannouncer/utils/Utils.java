package me.xxben.simpleannouncer.utils;

import org.bukkit.ChatColor;

public class Utils {

    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
