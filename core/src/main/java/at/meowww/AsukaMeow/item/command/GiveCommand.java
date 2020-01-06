package at.meowww.AsukaMeow.item.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand {

    public static boolean onCommand(
            ItemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        executor.itemManager.givePlayer((Player) commandSender, args[1]);
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
        return keys;
    }

}
