package at.meowww.AsukaMeow.territory.command;

import at.meowww.AsukaMeow.territory.Territory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand {

    public static boolean onCommand(
            TerritoryCommandExecutor executor,
            CommandSender commandSender,
            Command command, String s,
            String[] args) {
        String output = "Territory:\n[Id]  -  [Territory Title]\n";
        for (Territory territory : executor.territoryManager.getTerritoryMap().values())
            output += territory.getId() + "  -  " +
                    territory.getTitle() + "\n";
        commandSender.sendMessage(output);
        return true;
    }

    public static List<String> onTabComplete(
            TerritoryCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        return null;
    }
}
