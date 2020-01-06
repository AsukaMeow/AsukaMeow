package at.meowww.AsukaMeow.item.command;

import at.meowww.AsukaMeow.AsukaMeow;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
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
        AsukaMeow.INSTANCE.getNMSManager().getItemFactory().updateOnlinePlayerInventory();
        commandSender.sendMessage("Player inventory updated! ");
        return true;
    }

    public static List<String> onTabComplete(
            ItemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        List<String> keys = new ArrayList<>();
        executor.itemManager.getItemStackMap().keySet()
                .forEach(key -> keys.add(key.toString()));
        keys.add("all");
        return keys;
    }

}
