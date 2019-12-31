package at.meowww.AsukaMeow.System;

import at.meowww.AsukaMeow.database.IMongoStorable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

public class SystemManager implements IMongoStorable {

    private MongoCollection systemCol;

    private Announcement announcement = new Announcement();

    @Override
    public void initCollection(MongoDatabase db) {
        systemCol = db.getCollection("system");
    }

    public void load () {
        Document announcementDoc = (Document) systemCol.find(
                new Document("section", "announcement")).first();
        announcement.load(
                announcementDoc == null ? new Document() : announcementDoc);
    }

    public void save () {
        systemCol.replaceOne(
                new Document("section", "announcement"),
                announcement.save(),
                new ReplaceOptions().upsert(true));
    }

    public Announcement getAnnouncement () {
        return announcement;
    }

}
