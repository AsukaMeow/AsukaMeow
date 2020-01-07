package at.meowww.AsukaMeow.item.command;

import at.meowww.AsukaMeow.AsukaMeow;
import org.bukkit.NamespacedKey;
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
        if (args.length == 1 || args[1].equalsIgnoreCase("all")) {
            executor.itemManager.loadItems();
            commandSender.sendMessage("Reloaded "
                    + executor.itemManager.getItemStackMap().size()
                    + " items.");
        } else if (args.length == 2) {
            String[] keys = args[1].split(":");
            NamespacedKey key = new NamespacedKey(keys[0], keys[1]);
            if (!executor.itemManager.getItemStackMap().containsKey(key)) {
                commandSender.sendMessage("Item with key [" +
                        key.toString() + "] is not exists, can not reload.");
                return true;
            } else {
                executor.itemManager.loadItem(key);
                commandSender.sendMessage("Item with key [" +
                        key.toString() + "] reloaded!");
            }
        }
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
