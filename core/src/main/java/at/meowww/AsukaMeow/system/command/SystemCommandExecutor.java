package at.meowww.AsukaMeow.system.command;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.system.SystemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class SystemCommandExecutor implements CommandExecutor, TabCompleter {

    protected final AsukaMeow plugin;
    protected final SystemManager systemManager;

    public SystemCommandExecutor (
            AsukaMeow plugin, SystemManager systemManager) {
        this.plugin = plugin;
        this.systemManager = systemManager;
    }

    public void setExecutor () {
        plugin.getCommand("asuka-system").setExecutor(this);
        plugin.getLogger().info(
                "Command [asuka-system] executor set!");
    }

    @Override
    public boolean onCommand(
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        if (!commandSender.isOp())
            return false;

        if (args[0].equalsIgnoreCase("reload")) {
            return ReloadCommand.onCommand(this, commandSender, command, s, args);
        } else if (args[0].equalsIgnoreCase("save")) {
            return SaveCommand.onCommand(this, commandSender, command, s, args);
        } else if (args[0].equalsIgnoreCase("entrance")) {
            return EntranceCommand.onCommand(this, commandSender, command, s, args);
        } else if (args[0].equalsIgnoreCase("announcement")) {
            return AnnouncementCommand.onCommand(this, commandSender, command, s, args);
        } else {
            return false;
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.isOp())
            return null;

        switch (args[0].toLowerCase()) {
            case "reload":
                return ReloadCommand.onTabComplete(
                        this, commandSender, command, s, args);
            case "save":
                return SaveCommand.onTabComplete(
                        this, commandSender, command, s, args);
            case "entrance":
                return EntranceCommand.onTabComplete(
                        this, commandSender, command, s, args);
            case "announcement":
                return AnnouncementCommand.onTabComplete(
                        this, commandSender, command, s, args);
            default:
                return Arrays.asList(new String[] {
                        "reload", "save", "entrance", "announcement",
                });
        }
    }
}
