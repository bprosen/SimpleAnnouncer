package me.xxben.simpleannouncer.commands;

import me.xxben.simpleannouncer.SimpleAnnouncer;
import me.xxben.simpleannouncer.managers.ConfigManager;
import me.xxben.simpleannouncer.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.Arrays;

public class SimpleAnnouncerCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] a) {

        // command to announce to all online players any string
        if (a.length >= 1 && a[0].equalsIgnoreCase("announce")) {

            String announcement = String.join(" ", Arrays.copyOfRange(a, 2, a.length));
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Utils.translate(announcement)));
        } else if (a.length == 1 && a[0].equalsIgnoreCase("reload")) {
            // attempt to reload config
            try {
                // load config and refresh manager
                ConfigManager configManager = SimpleAnnouncer.getConfigManager();

                configManager.load("config");
                SimpleAnnouncer.getSettingsManager().load(configManager.get("config"));
                SimpleAnnouncer.getAnnouncementManager().refreshManager(SimpleAnnouncer.getPlugin());

                sender.sendMessage(Utils.translate("&aSuccessfully reloaded the config"));
            } catch (Exception exception) {
                sender.sendMessage(Utils.translate("&cError reloading the config, check console"));
                exception.printStackTrace();
            }
        } else if (a.length == 1 && a[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
        } else {
            sendHelp(sender);
        }
         return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Utils.translate("&4&lSimple Announcer Help"));
        sender.sendMessage(Utils.translate(" &8> &c/simpleannouncer announce (Announcement...) &7- Announce to online players with color"));
        sender.sendMessage(Utils.translate(" &8> &c/simpleannouncer reload &7- Reloads from config (restarts scheduler)"));
        sender.sendMessage(Utils.translate(" &8> &c/simpleannouncer help &7- Displays this page"));
    }
}
