package at.meowww.AsukaMeow.territory.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand {

    public static boolean onCommand(
            TerritoryCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        if (args.length == 1 || args[1].equalsIgnoreCase("all")) {
            executor.territoryManager.loadTerritories();
            commandSender.sendMessage("Reloaded "
                    + executor.territoryManager.getTerritoryMap().size()
                    + " dialogs.");
        } else if (args.length == 2) {
            if (!executor.territoryManager.getTerritoryMap().containsKey(args[1]))
                commandSender.sendMessage("Territory with id [" +
                        args[1] + "] is not exists, can not reload.");
            else {
                executor.territoryManager.loadTerritory(args[1]);
                commandSender.sendMessage("Territory with id [" +
                        args[1] + "] reloaded!");
            }
        }
        return true;
    }

    public static List<String> onTabComplete(
            TerritoryCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        List<String> keys = new ArrayList<>(
                executor.territoryManager.getTerritoryMap().keySet());
        keys.add("all");
        return keys;
    }

}
