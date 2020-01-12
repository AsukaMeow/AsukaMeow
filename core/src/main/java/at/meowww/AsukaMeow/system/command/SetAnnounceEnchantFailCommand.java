package at.meowww.AsukaMeow.system.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SetAnnounceEnchantFailCommand {

    public static boolean onCommand(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {

        executor.systemManager.setAnnounceEnchantFail(
                !executor.systemManager.isAnnounceEnchantFail());
        commandSender.sendMessage("System announceEnchantFail set to ["
                + executor.systemManager.isAnnounceEnchantFail() + "].");
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
