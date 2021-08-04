package me.xxben.simpleannouncer.managers;

import org.bukkit.configuration.file.FileConfiguration;

public class SettingsManager {

    public boolean RANDOM;
    public int ANNOUNCEMENT_TIMER;

    public SettingsManager(FileConfiguration config) {
        load(config);
    }

    // load all needed settings into cache
    public void load(FileConfiguration config) {
        RANDOM = config.getBoolean("settings.random-announcements");
        ANNOUNCEMENT_TIMER = config.getInt("settings.announcement-timer");
    }
}
