package at.meowww.AsukaMeow.system;

import at.meowww.AsukaMeow.AsukaMeow;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

public class Entrance {

    private boolean shuttingdown = false;
    private boolean allow;
    private String disallowMsg;

    public boolean isAllow () {
        return this.allow && !this.shuttingdown;
    }

    public String getDisallowMsg () {
        return this.disallowMsg;
    }

    public void entranceShutdown() {
        this.shuttingdown = true;
    }

    public void setAllow(boolean value) {
        this.allow = value;
    }

    public void setDisallowMsg(String value) {
        this.disallowMsg = value;
    }

    public void load (MongoCollection systemCol) {
        Document document = (Document) systemCol.find(
                new Document("section", "entrance")).first();
        if (document == null)
            document = new Document("content", new Document());

        Document contentDoc = document.get("content", new Document());

        allow = contentDoc.get("allow", false);
        disallowMsg = contentDoc.get("disallow-msg", "");
        AsukaMeow.INSTANCE.getLogger().info("System Entrance Loaded");
    }

    public void save (MongoCollection systemCol) {
        systemCol.replaceOne(
                new Document("section", "entrance"),
                new Document("section", "entrance")
                        .append("content",
                                new Document("allow", allow)
                                        .append("disallow-msg", disallowMsg)
                        ),
                new ReplaceOptions().upsert(true));
        AsukaMeow.INSTANCE.getLogger().info("System Entrance Loaded");
    }

}
