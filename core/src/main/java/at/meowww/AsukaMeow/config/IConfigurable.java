package at.meowww.AsukaMeow.config;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfigurable {

    void writeDefaultConfigValues (ConfigurationSection parentSection);
    void readConfigValues (ConfigurationSection parentSection);
    void writeConfigValues (ConfigurationSection parentSection);

}
