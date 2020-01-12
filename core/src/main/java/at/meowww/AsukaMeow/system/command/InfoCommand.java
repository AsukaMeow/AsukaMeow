package at.meowww.AsukaMeow.system.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InfoCommand {

    public static boolean onCommand(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {

        String output = "System Info:\n";
        output += "CanBreed: " + executor.systemManager.isCanBreed() + "\n";
        output += "CanEnchant: " + executor.systemManager.isCanEnchant() + "\n";
        output += "AnnounceEnchantFail: " + executor.systemManager.isAnnounceEnchantFail() + "\n";
        output += "EnchantSuccessChance: " + executor.systemManager.getEnchantSuccessChance() + "\n";
        commandSender.sendMessage(output);
        return true;
    }

    public static List<String> onTabComplete(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        return null;
    }
}
