package at.meowww.AsukaMeow.config;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Interface for any class whom need config saving
 * @author clooooode
 * @since 0.0.1-SNAPSHOT
 */
public interface IConfigurable {

    /**
     * Write default value into parentSection
     *
     * There is no any files been write during this function.
     * Writing action is manage by {@link ConfigManager#save}.
     * Sometimes this function may contains same code with {@link #writeConfigValues}
     * @param parentSection The parent section to be write.
     */
    void writeDefaultConfigValues (ConfigurationSection parentSection);

    /**
     * Read value from config
     * @param parentSection The parent section to be read from.
     */
    void readConfigValues (ConfigurationSection parentSection);

    /**
     * Write value into config
     * @param parentSection The parent section to be write.
     */
    void writeConfigValues (ConfigurationSection parentSection);

}
