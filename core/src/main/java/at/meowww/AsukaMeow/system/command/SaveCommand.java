package at.meowww.AsukaMeow.system.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SaveCommand {

    public static boolean onCommand(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        executor.systemManager.save();
        commandSender.sendMessage("System value saved to database!");
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
