package at.meowww.AsukaMeow.system.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class EntranceCommand {

    public static boolean onCommand(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {

        if (args[1].equalsIgnoreCase("info")) {
            String output = "Entrance Info:\n";
            output += "Allow: " + executor.systemManager.getEntrance().isAllow() + "\n";
            output += "Disallow Msg: " + executor.systemManager.getEntrance().getDisallowMsg() + "\n";
            commandSender.sendMessage(output);
        } else if (args[1].equalsIgnoreCase("set-allow")) {
            executor.systemManager.getEntrance().setAllow(
                    !executor.systemManager.getEntrance().isAllow()
            );
            commandSender.sendMessage("Entrance allow set to ["
                    + executor.systemManager.getEntrance().isAllow()
                    + "].");
        } else if (args[1].equalsIgnoreCase("set-disallow-msg")) {
            executor.systemManager.getEntrance().setDisallowMsg(args[2]);
            commandSender.sendMessage("Disallow msg set!");
        }

        return true;
    }

    public static List<String> onTabComplete(
            SystemCommandExecutor executor,
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        return Arrays.asList(new String[] {
                "info", "set-allow", "set-disallow-msg",
        });
    }
}
