package at.meowww.AsukaMeow.database;

import at.meowww.AsukaMeow.config.IConfigurable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bukkit.configuration.ConfigurationSection;

public class DatabaseManager implements IConfigurable {

    private ConfigurationSection dbSection;

    private String host;
    private String dbName;

    private MongoClient mgoCli;
    private MongoDatabase db;

    public DatabaseManager () {}

    public void databaseInit (IMongoStorable ... storable) {
        mgoCli = MongoClients.create(host);
        db = mgoCli.getDatabase(dbName);
        for (IMongoStorable s : storable) {
            s.initCollection(db);
        }
    }

    @Override
    public void writeDefaultConfigValues(ConfigurationSection parentSection) {
        dbSection = parentSection.createSection("Database");
        dbSection.set("Host", "mongodb://localhost:27017");
        dbSection.set("DbName", "AsukaMeow");
    }

    @Override
    public void readConfigValues(ConfigurationSection parentSection) {
        dbSection = parentSection.getConfigurationSection("Database");
        host = dbSection.getString("Host");
        dbName = dbSection.getString("DbName");
    }

    @Override
    public void writeConfigValues(ConfigurationSection parentSection) {
        dbSection.set("Host", host);
        dbSection.set("DbName", dbName);
        parentSection.set("Database", dbSection);
    }
}
