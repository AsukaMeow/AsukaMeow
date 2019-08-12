package at.meowww.AsukaMeow.util;

import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public abstract class Listener implements org.bukkit.event.Listener {

    protected Handler handler;

    public Listener (Handler handler) {
        this.handler = handler;
    }

    public void register (JavaPlugin plugin) {
        getServer().getPluginManager().registerEvents(this, plugin);
    }

}
