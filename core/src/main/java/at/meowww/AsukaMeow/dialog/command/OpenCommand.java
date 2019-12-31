package at.meowww.AsukaMeow.dialog.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpenCommand {

    public static boolean onCommand(
            DialogCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("This command only execute in-game.");
            return true;
        }

        UUID uuid = UUID.fromString(args[1]);
        if (!executor.dialogManager.getDialogMap().containsKey(uuid))
            commandSender.sendMessage("No such UUID");
        else
            executor.plugin
                    .getNMSManager()
                    .getDialogFactory()
                    .openDialog(
                            (Player) commandSender,
                            executor.dialogManager.getDialogMap().get(uuid)
                    );
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
        return keys;
    }
}
