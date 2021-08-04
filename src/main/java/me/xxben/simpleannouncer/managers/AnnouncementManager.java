package me.xxben.simpleannouncer.managers;

import me.xxben.simpleannouncer.SimpleAnnouncer;
import me.xxben.simpleannouncer.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class AnnouncementManager {

    private LinkedHashMap<Integer, List<String>> announcementsMap;
    private BukkitTask announcementTimer;
    private int previousAnnouncementIndex = 1; // if they are not doing random, keep track of the previous

    public AnnouncementManager(Plugin plugin) {
        announcementsMap = getAnnouncementsFromConfig();
        startScheduler(plugin);
    }

    public void refreshManager(Plugin plugin) {
        // clear cache, cancel task and reset previous index
        announcementsMap.clear();
        announcementTimer.cancel();
        previousAnnouncementIndex = 1;

        // then start it back up again
        announcementsMap = getAnnouncementsFromConfig();
        startScheduler(plugin);
    }

    // schedulers
    private void startScheduler(Plugin plugin) {
        SettingsManager settingsManager = SimpleAnnouncer.getSettingsManager();

        // scheduler for timed announcements
        announcementTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (!announcementsMap.isEmpty()) {
                    List<String> announcement;

                    // if random, simply choose random announcement, otherwise, do next according to index
                    if (settingsManager.RANDOM)
                        announcement = chooseRandomAnnouncement();
                    else {
                        announcement = announcementsMap.get(previousAnnouncementIndex);

                        // if at max, reset to 1
                        if (previousAnnouncementIndex == announcementsMap.size())
                            previousAnnouncementIndex = 1;
                        else
                            previousAnnouncementIndex++;
                    }

                    // send msg to all online players if announcement not null
                    if (announcement != null)
                        for (String announcementString : announcement)
                                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Utils.translate(announcementString)));
                }
            }
        }.runTaskTimer(plugin, settingsManager.ANNOUNCEMENT_TIMER * 20, settingsManager.ANNOUNCEMENT_TIMER * 20);
    }

    // get from config
    public LinkedHashMap<Integer, List<String>> getAnnouncementsFromConfig() {
        LinkedHashMap<Integer, List<String>> temporaryList = new LinkedHashMap<>();
        FileConfiguration config = SimpleAnnouncer.getConfigManager().get("config");

        // if int is a section, add to list!
        for (int i = 1;; i++)
            if (config.isList("settings.announcements." + i))
                temporaryList.put(i, config.getStringList("settings.announcements." + i));
            else
                break; // break if no section so for loop stops

        return temporaryList;
    }

    // chooses random announcement if there is any to choose from
    public List<String> chooseRandomAnnouncement() {
        return announcementsMap.isEmpty() ? null : announcementsMap.get(new Random().nextInt(announcementsMap.size() + 1)); // + 1 due to random going 1 below
    }

    public BukkitTask getAnnouncementTimerTask() { return announcementTimer; }

    public LinkedHashMap<Integer, List<String>> getAnnouncementsList() { return announcementsMap; }
}
