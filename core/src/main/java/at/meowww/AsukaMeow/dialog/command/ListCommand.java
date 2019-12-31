package at.meowww.AsukaMeow.dialog.command;

import at.meowww.AsukaMeow.dialog.Dialog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand {

    public static boolean onCommand(
            DialogCommandExecutor executor,
            CommandSender commandSender,
            Command command, String s,
            String[] args) {
        String output = "Dialogs:\n[UUID]  -  [Dialog Title]\n";
        for (Dialog d : executor.dialogManager.getDialogMap().values())
            output += d.getUUID().toString() + "  -  " +
                    d.getTitle().toLegacyText() + "\n";
        commandSender.sendMessage(output);
        return true;
    }

    public static List<String> onTabComplete(
            DialogCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        return null;
    }
}
