package at.meowww.AsukaMeow.item.command;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.item.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class ItemCommandExecutor implements CommandExecutor, TabCompleter {

    protected final AsukaMeow plugin;
    protected final ItemManager itemManager;

    public ItemCommandExecutor(
            AsukaMeow plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    public void setExecutor () {
        plugin.getCommand("asuka-item").setExecutor(this);
        plugin.getLogger().info(
                "Command [asuka-item] executor set!");
    }

    @Override
    public boolean onCommand(
            CommandSender commandSender,
            Command command,
            String s,
            String[] args
    ) {
        if (!commandSender.isOp())
            return false;

        if (args[0].equalsIgnoreCase("reload")) {
            return ReloadCommand.onCommand(
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
            String[] args
    ) {
        if (!commandSender.isOp())
            return null;

        switch (args[0].toLowerCase()) {
            case "reload":
                return ReloadCommand.onTabComplete(
                        this, commandSender, command, s, args);
            default:
                return Arrays.asList(new String[] {
                        "reload",
                });
        }
    }

}
