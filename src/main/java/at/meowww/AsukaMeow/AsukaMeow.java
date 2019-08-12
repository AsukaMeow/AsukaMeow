package at.meowww.AsukaMeow;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class AsukaMeow extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("Minecraft");
    public static AsukaMeow INSTANCE;

    public static ConfigManager configManager;

    @Override
    public void onEnable () {
        INSTANCE = this;
        configManager = new ConfigManager(INSTANCE);

        configManager.loadConfig();
    }

    @Override
    public void onDisable () {
        configManager.saveConfig();
    }

}
