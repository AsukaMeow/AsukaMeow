package at.meowww.AsukaMeow.dialog.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReloadCommand {

    public static boolean onCommand(
            DialogCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        if (args.length == 1 || args[1].equalsIgnoreCase("all")) {
            executor.dialogManager.loadDialogs();
            commandSender.sendMessage("Reloaded "
                    + executor.dialogManager.getDialogMap().size()
                    + " dialogs.");
        } else if (args.length == 2) {
            UUID uuid = UUID.fromString(args[1]);
            if (!executor.dialogManager.getDialogMap().containsKey(uuid))
                commandSender.sendMessage("Dialog with UUID [" +
                        uuid + "] is not exists, can not reload.");
            else {
                executor.dialogManager.loadDialog(uuid);
                commandSender.sendMessage("Dialog with UUID [" +
                        uuid + "] reloaded!");
            }
        }
        return true;
    }

    public static List<String> onTabComplete(
            DialogCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        List<String> keys = new ArrayList<>();
        executor.dialogManager.getDialogMap().keySet()
                .forEach(uuid -> keys.add(uuid.toString()));
        keys.add("all");
        return keys;
    }

}
