package at.meowww.AsukaMeow;

import at.meowww.AsukaMeow.config.ConfigManager;
import at.meowww.AsukaMeow.database.DatabaseManager;
import at.meowww.AsukaMeow.nms.NMSManager;
import at.meowww.AsukaMeow.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class AsukaMeow extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("");
    public static String NMS_VERSION;
    public static AsukaMeow INSTANCE;

    private File file;
    private FileConfiguration config;

    private ConfigManager configManager;
    private DatabaseManager databaseManager;

    private NMSManager nmsMamager;
    private UserManager userManager;
    private World defaultWorld;

    @Override
    public void onEnable () {
        NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        INSTANCE = this;
        file = new File(this.getDataFolder(), "config.yml");
        config = new YamlConfiguration();
        configManager = new ConfigManager(file, config);
        databaseManager = new DatabaseManager();

        nmsMamager = new NMSManager();
        userManager = new UserManager();

        configManager.load(databaseManager);
        logger.info("AsukaMeow config loaded!");

        databaseManager.databaseInit(userManager);
        logger.info("AsukaMeow database loaded!");

        userManager.registerListener();
        logger.info("AsukaMeow UserManager loaded!");

        defaultWorld = Bukkit.getWorlds().get(0);
    }

    @Override
    public void onDisable () {
        configManager.save(databaseManager);
    }

    public World getDefaultWorld () {
        return this.defaultWorld;
    }

    public NMSManager getNMSManager () {
        return this.nmsMamager;
    }
}
