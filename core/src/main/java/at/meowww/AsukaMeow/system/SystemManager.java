package at.meowww.AsukaMeow.system;

import at.meowww.AsukaMeow.database.IMongoStorable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SystemManager implements IMongoStorable {

    private MongoCollection systemCol;

    private Announcement announcement = new Announcement();

    @Override
    public void initCollection(MongoDatabase db) {
        systemCol = db.getCollection("system");
    }

    public void load () {
        announcement.load(systemCol);
    }

    public void save () {
        announcement.save(systemCol);
    }

    public Announcement getAnnouncement () {
        return announcement;
    }

}
