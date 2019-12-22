package at.meowww.AsukaMeow;

import at.meowww.AsukaMeow.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class AsukaMeow extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("");
    public static AsukaMeow INSTANCE;

    private File file;

    private FileConfiguration config;

    private ConfigManager configManager;

    @Override
    public void onEnable () {
        INSTANCE = this;
        file = new File(this.getDataFolder(), "config.yml");
        config = new YamlConfiguration();
        configManager = new ConfigManager(file, config);

        configManager.load();
        logger.info("AsukaMeow config loaded!");
    }

    @Override
    public void onDisable () {
        configManager.save();
    }

}
