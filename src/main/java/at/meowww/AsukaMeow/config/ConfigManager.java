package at.meowww.AsukaMeow.config;

import at.meowww.AsukaMeow.AsukaMeow;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfigManager implements IConfigurable {

    private Logger logger;
    private File file;
    private FileConfiguration config;
    private ConfigurationSection meowSection;

    public boolean debug = false;

    public ConfigManager (File file, FileConfiguration config) {
        this.logger = AsukaMeow.logger;
        this.file = file;
        this.config = config;
    }

    public void load (IConfigurable ... configurables) {
        try {
            if (file.exists() == false) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                writeDefaultConfigValues(config);
                for (IConfigurable c : configurables) {
                    c.writeDefaultConfigValues(config);
                }
                config.save(file);
            }

            config.load(file);
            readConfigValues(config);
            for (IConfigurable c : configurables) {
                c.readConfigValues(config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save (IConfigurable ... configurables) {
        try {
            writeConfigValues(config);
            for (IConfigurable c : configurables) {
                c.writeConfigValues(config);
            }
            config.save(file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void writeDefaultConfigValues(ConfigurationSection parentSection) {
        meowSection = parentSection.createSection("AsukaMeow");
        meowSection.set("Debug", debug);
        logger.info("Detect AsukaMeow config missing: AsukaMeow config write complete!");
    }

    @Override
    public void readConfigValues(ConfigurationSection parentSection) {
        meowSection = parentSection.getConfigurationSection("AsukaMeow");
        debug = meowSection.getBoolean("Debug");
        logger.info("AsukaMeow config read complete!");
    }

    @Override
    public void writeConfigValues(ConfigurationSection parentSection) {
        meowSection.set("Debug", debug);
        parentSection.set("AsukaMeow", meowSection);
        logger.info("AsukaMeow config write complete!");
    }
}
