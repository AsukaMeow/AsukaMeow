package at.meowww.AsukaMeow.system;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.database.IMongoStorable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bukkit.Bukkit;

public class SystemManager implements IMongoStorable {

    private SystemListener listener;

    private MongoCollection systemCol;

    private Entrance entrance = new Entrance();
    private Announcement announcement = new Announcement();

    public SystemManager() {
        this.listener = new SystemListener(this);
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(listener, AsukaMeow.INSTANCE);
    }

    @Override
    public void initCollection(MongoDatabase db) {
        systemCol = db.getCollection("system");
    }

    public void load () {
        entrance.load(systemCol);
        announcement.load(systemCol);
    }

    public void save () {
        entrance.save(systemCol);
        announcement.save(systemCol);
    }

    public Entrance getEntrance () {
        return this.entrance;
    }

    public Announcement getAnnouncement () {
        return announcement;
    }

}
