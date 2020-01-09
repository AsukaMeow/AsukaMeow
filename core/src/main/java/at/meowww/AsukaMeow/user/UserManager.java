package at.meowww.AsukaMeow.user;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.database.IMongoStorable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager implements IMongoStorable {

    private UserListener listener;

    private MongoCollection userCol;

    public Map<UUID, User> onlineUser = new HashMap<>();

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

    public User getUser (UUID uuid) {
        return this.onlineUser.get(uuid);
    }

    public Map<UUID, User> getOnlineUser () {
        return this.onlineUser;
    }

    /**
     * This method is to port old player which currently missing database file of inventory.
     * To prevent there in-game inventory been clear.
     * Grab their saves data from Minecraft vanilla and store into database.
     *
     * This should deprecated soon.
     */
    public void portOldPlayer () {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            UUID uuid = player.getUniqueId();
            User offlineUser = findUser(uuid);

            // User is exists &
            // Player inventory not empty -> means its inventory not been override by us before.
            if (offlineUser != null && offlineUser.getUserInventory().allEmpty()) {
                offlineUser.setUserInventory(AsukaMeow.INSTANCE
                        .getNMSManager()
                        .getPlayerFactory()
                        .getPlayerInventory(player.getUniqueId()));
                updateUser(offlineUser);
            }
        }
    }
}
