package at.meowww.AsukaMeow.dialog.command;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.dialog.DialogManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class DialogCommandExecutor implements CommandExecutor, TabCompleter {

    protected final AsukaMeow plugin;
    protected final DialogManager dialogManager;

    public DialogCommandExecutor(
            AsukaMeow plugin, DialogManager dialogManager) {
        this.plugin = plugin;
        this.dialogManager = dialogManager;
    }

    public void setExecutor () {
        plugin.getCommand("asuka-dialog").setExecutor(this);
        plugin.getLogger().info(
                "Command [asuka-dialog] executor set!");
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
            return ReloadCommand.onCommand(
                    this, commandSender, command, s, args);
        } else if (args[0].equalsIgnoreCase("list")) {
            return ListCommand.onCommand(
                    this, commandSender, command, s, args);
        } else if (args[0].equalsIgnoreCase("open")) {
            return OpenCommand.onCommand(
                    this, commandSender, command, s, args);
        } else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(
            CommandSender commandSender,
            Command command,
            String s,
            String[] args) {
        if (!commandSender.isOp())
            return null;

        switch (args[0].toLowerCase()) {
            case "reload":
                return ReloadCommand.onTabComplete(
                        this, commandSender, command, s, args);
            case "list":
                return ListCommand.onTabComplete(
                        this, commandSender, command, s, args);
            case "open":
                return OpenCommand.onTabComplete(
                        this, commandSender, command, s, args);
            default:
                return Arrays.asList(new String[] {
                    "reload", "list", "open",
            });
        }
    }
}
