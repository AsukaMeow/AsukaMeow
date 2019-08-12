package at.meowww.AsukaMeow.util;

import org.bukkit.configuration.MemorySection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public abstract class Handler {


    protected Listener listener;

    protected MemorySection config;
    protected boolean enable;

    public Handler () {}

    public boolean getEnable () {
        return this.enable;
    }

    public void flipEnable () {
        this.setEnable(!this.enable);
    }

    public void setEnable (boolean val) {
        this.enable = val;
    }

    public void load (MemorySection config) {}

    public MemorySection save () {
        return null;
    }

    public void toggleRegister (JavaPlugin plugin) {
        if (this.enable) {
            getServer().getPluginManager().registerEvents(this.listener, plugin);
        } else {
            HandlerList.unregisterAll(this.listener);
        }
    }

}
