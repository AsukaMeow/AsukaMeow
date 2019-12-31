package at.meowww.AsukaMeow.system.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AnnouncementCommand {

    public static boolean onCommand(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {

        if (args[1].equalsIgnoreCase("set-dialog")) {
            UUID uuid = UUID.fromString(args[2]);
            if (!executor.plugin.getDialogManager().getDialogMap().containsKey(uuid))
                commandSender.sendMessage("Dialog UUID [" + args[2] + "] is not exists.");
            else {
                executor.systemManager.getAnnouncement().setDialogUUID(uuid);
                commandSender.sendMessage("Dialog UUID set to [" + args[2] + "].");
            }
        } else if (args[1].equalsIgnoreCase("set-start-date")) {
        }

        return true;
    }

    public static List<String> onTabComplete(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        if (args[1].equalsIgnoreCase("set-dialog")) {
            List<String> keys = new ArrayList<>();
            executor.plugin.getDialogManager().getDialogMap().keySet()
                    .forEach(uuid -> keys.add(uuid.toString()));
            return keys;
        } else {
            return Arrays.asList(new String[] {
                    "set-dialog",
            });
        }
    }
}
