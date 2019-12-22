package at.meowww.AsukaMeow.user;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.database.IMongoStorable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager implements IMongoStorable {

    private UserListener listener;

    private MongoCollection userCol;

    public Map<UUID, User> onlineUser = new HashMap<UUID, User>();

    public UserManager () {
        listener = new UserListener(this);
    }

    public void registerListener () {
        Bukkit.getServer().getPluginManager().registerEvents(listener, AsukaMeow.INSTANCE);
    }

    @Override
    public void initCollection(MongoDatabase db) {
        userCol = db.getCollection("user");
    }

    public User findUser (UUID uuid) {
        Object obj = userCol.find(new Document("uuid", uuid.toString())).first();
        return obj == null ? null : User.fromDocument((Document) obj);
    }

    public void updateUser (User user) {
        userCol.replaceOne(
                new Document("uuid", user.getUUID().toString()),
                User.toDocument(user),
                new ReplaceOptions().upsert(true));
    }
}
