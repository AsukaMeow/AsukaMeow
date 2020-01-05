package at.meowww.AsukaMeow.item.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand {

    public static boolean onCommand(
            ItemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        executor.itemManager.loadItems();
        commandSender.sendMessage("Reloaded "
                + executor.itemManager.getItemStackMap().size()
                + " items.");
        return true;
    }

    public static List<String> onTabComplete(
            ItemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        return null;
    }

}
