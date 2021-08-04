package me.xxben.simpleannouncer;

import me.xxben.simpleannouncer.commands.SimpleAnnouncerCMD;
import me.xxben.simpleannouncer.managers.AnnouncementManager;
import me.xxben.simpleannouncer.managers.ConfigManager;
import me.xxben.simpleannouncer.managers.SettingsManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class SimpleAnnouncer extends JavaPlugin {

    private static SimpleAnnouncer plugin;
    private static Logger logger;

    // managers
    private static AnnouncementManager announcementManager;
    private static SettingsManager settingsManager;
    private static ConfigManager configManager;

    @Override
    public void onEnable() {
        plugin = this;
        logger = getLogger();

        loadManagers();
        registerCommands();

        logger.info("SimpleAnnouncer Enabled");
    }

    @Override
    public void onDisable() {

        unloadManagers();

        logger.info("SimpleAnnouncer Disabled");
        plugin = null;
    }

    private void registerCommands() {
        getCommand("announcer").setExecutor(new SimpleAnnouncerCMD());
    }

    // enable and load all managers
    private void loadManagers() {
        configManager = new ConfigManager(this);
        settingsManager = new SettingsManager(configManager.get("config"));
        announcementManager = new AnnouncementManager(this);
    }

    // null all managers
    private void unloadManagers() {
        announcementManager = null;
        settingsManager = null;
        configManager = null;
    }

    public static AnnouncementManager getAnnouncementManager() { return announcementManager; }
    public static ConfigManager getConfigManager() { return configManager; }
    public static SettingsManager getSettingsManager() { return settingsManager; }

    public static SimpleAnnouncer getPlugin() { return plugin; }

    public static Logger getPluginLogger() { return logger; }
}
