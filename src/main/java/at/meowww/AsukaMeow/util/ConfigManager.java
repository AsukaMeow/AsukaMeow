package at.meowww.AsukaMeow.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ConfigManager {

    protected JavaPlugin plugin;
    protected FileConfiguration config;


    public ConfigManager (JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig () {
        this.config = this.plugin.getConfig();
        this.config.options().copyDefaults(true);
        this.plugin.saveConfig();
    }

    public void reloadConfig () {
        this.loadConfig();
    }

    public void saveConfig () {
        this.plugin.saveConfig();
    }

}
