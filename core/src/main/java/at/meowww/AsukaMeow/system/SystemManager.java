package at.meowww.AsukaMeow.system;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.database.IMongoStorable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

public class SystemManager implements IMongoStorable {

    private SystemListener listener;

    private MongoCollection systemCol;

    private Entrance entrance = new Entrance();
    private Announcement announcement = new Announcement();

    private boolean canBreed;
    private boolean canEnchant;
    private boolean announceEnchantFail;
    private double enchantSuccessChance;

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

    public boolean isCanBreed() {
        return this.canBreed;
    }

    public boolean isCanEnchant() {
        return this.canEnchant;
    }

    public boolean isAnnounceEnchantFail () {
        return this.announceEnchantFail;
    }

    public double getEnchantSuccessChance() {
        return this.enchantSuccessChance;
    }

    public void setCanBreed(boolean value) {
        this.canBreed = value;
    }

    public void setCanEnchant(boolean value) {
        this.canEnchant = value;
    }

    public void setAnnounceEnchantFail(boolean value) {
        this.announceEnchantFail = value;
    }

    public void setEnchantSuccessChance(float value) {
        this.enchantSuccessChance = value;
    }

    public void load () {
        entrance.load(systemCol);
        announcement.load(systemCol);

        Document document = (Document) systemCol.find(
                new Document("section", "system")).first();
        if (document == null)
            document = new Document("content", new Document());

        Document contentDoc = document.get("content", new Document());

        canBreed = contentDoc.get("can_breed", false);
        canEnchant = contentDoc.get("can_enchant", true);
        announceEnchantFail = contentDoc.get("announce_enchant_fail", true);
        enchantSuccessChance = contentDoc.get("enchant_success_chance", 0.02D);
        AsukaMeow.INSTANCE.getLogger().info("System Loaded");
    }

    public void save () {
        entrance.save(systemCol);
        announcement.save(systemCol);

        systemCol.replaceOne(
                new Document("section", "system"),
                new Document("section", "system").append(
                        "content",
                        new Document("can_breed", canBreed)
                                .append("can_enchant", canEnchant)
                                .append("announce_enchant_fail", announceEnchantFail)
                                .append("enchant_success_chance", enchantSuccessChance)
                ),
                new ReplaceOptions().upsert(true));
        AsukaMeow.INSTANCE.getLogger().info("System Saved");
    }

    public Entrance getEntrance () {
        return this.entrance;
    }

    public Announcement getAnnouncement () {
        return announcement;
    }

}
