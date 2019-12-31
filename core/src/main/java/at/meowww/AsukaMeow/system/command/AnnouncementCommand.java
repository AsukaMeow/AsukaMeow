package at.meowww.AsukaMeow.system.command;

import at.meowww.AsukaMeow.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class AnnouncementCommand {

    public static boolean onCommand(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {

        if (args[1].equalsIgnoreCase("info")) {
            String output = "System Info:\n";
            output += "Dialog UUID: "
                    + (executor.systemManager.getAnnouncement().getDialogUUID() == null ?
                       null :
                       executor.systemManager.getAnnouncement().getDialogUUID().toString())
                    + "\n";
            output += "Start Date: "
                    + Utils.dateToString(executor.systemManager.getAnnouncement().getStartDate())
                    + "\n";
            output += "End Date: "
                    + Utils.dateToString(executor.systemManager.getAnnouncement().getEndDate())
                    + "\n";
            commandSender.sendMessage(output);
        } else if (args[1].equalsIgnoreCase("set-dialog")) {
            UUID uuid = UUID.fromString(args[2]);
            if (!executor.plugin.getDialogManager().getDialogMap().containsKey(uuid))
                commandSender.sendMessage("Dialog UUID [" + args[2] + "] is not exists.");
            else {
                executor.systemManager.getAnnouncement().setDialogUUID(uuid);
                commandSender.sendMessage("Dialog UUID set to [" + args[2] + "].");
            }
        } else if (args[1].equalsIgnoreCase("set-date")) {
            Date startDate = Utils.stringToDate(args[2]);
            if (startDate == null)
                commandSender.sendMessage(args[2]
                        + " is not a valid date string, should be YYYY-MM-DDHTT");
            else {
                executor.systemManager.getAnnouncement().setStartDate(startDate);
                commandSender.sendMessage("Start date is set to " + startDate);
            }

            if (args.length == 4) {
                Date endDate = Utils.stringToDate(args[3]);
                if (endDate == null)
                    commandSender.sendMessage(args[3]
                            + " is not a valid date string, should be YYYY-MM-DDHTT");
                else {
                    executor.systemManager.getAnnouncement().setEndDate(endDate);
                    commandSender.sendMessage("End date is set to " + endDate);
                }
            }
        }

        return true;
    }

    public static List<String> onTabComplete(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        if (args[1].equalsIgnoreCase("info")) {
            return null;
        } else if (args[1].equalsIgnoreCase("set-dialog")) {
            List<String> keys = new ArrayList<>();
            executor.plugin.getDialogManager().getDialogMap().keySet()
                    .forEach(uuid -> keys.add(uuid.toString()));
            return keys;
        } else if (args[1].equalsIgnoreCase("set-date")) {
            return Arrays.asList(new String[] {Utils.dateToString(new Date())});
        } else {
            return Arrays.asList(new String[] {
                    "info", "set-dialog", "set-date",
            });
        }
    }
}
