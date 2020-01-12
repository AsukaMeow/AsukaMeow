package at.meowww.AsukaMeow.system.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SetEnchantSuccessChance {

    public static boolean onCommand(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        try {
            executor.systemManager.setEnchantSuccessChance(
                    Float.parseFloat(args[1]));
            commandSender.sendMessage("System enchantSuccessChance set to ["
                    + executor.systemManager.getEnchantSuccessChance() + "].");
        } catch (NumberFormatException nfe) {
            commandSender.sendMessage(args[1] + " is not a valid float format number.");
        }
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
