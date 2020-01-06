package at.meowww.AsukaMeow.item;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.database.IMongoStorable;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ItemManager implements IMongoStorable {

    private ItemListener listener;

    private MongoCollection itemCol;
    private Map<NamespacedKey, AsukaItem> itemStackMap = new HashMap<>();

    public ItemManager () {
        this.listener = new ItemListener(this);
    }

    @Override
    public void initCollection(MongoDatabase db) {
        itemCol = db.getCollection("item");
    }

    public void registerListener () {
        Bukkit.getServer().getPluginManager().registerEvents(listener, AsukaMeow.INSTANCE);
    }

    public Map<NamespacedKey, AsukaItem> getItemStackMap () {
        return this.itemStackMap;
    }

    public AsukaItem getAsukaItem (String key) {
        String[] keys = key.split(":");
        return getAsukaItem(new NamespacedKey(keys[0], keys[1]));
    }

    public AsukaItem getAsukaItem (NamespacedKey key) {
        if (this.itemStackMap.containsKey(key))
            return this.itemStackMap.get(key);
        return null;
    }

    public void loadItems() {
        Document document;
        Map<NamespacedKey, AsukaItem> loadedItemStackMap = new HashMap<>();
        MongoCursor<Document> cursor = itemCol.find().cursor();
        while(cursor.hasNext()) {
            document = cursor.next();

            String[] id = document.getString("id").split(":");
            NamespacedKey key = new NamespacedKey(id[0], id[1]);
            loadedItemStackMap.put(key,
                    AsukaItem.deserialize(new JsonParser().parse(
                            document.toJson()).getAsJsonObject())
            );
            AsukaMeow.INSTANCE.getLogger().info("Item [" + key.toString() + "] loaded!");
        }
        itemStackMap = loadedItemStackMap;

        AsukaMeow.INSTANCE.getLogger().info("Loaded " + itemStackMap.size() + " items!");
    }

    public void saveItems() {
        itemStackMap.forEach((key, item) -> {
            itemCol.replaceOne(
                    new Document("id", key.toString()),
                    Document.parse(AsukaItem.serialize(item).toString()),
                    new ReplaceOptions().upsert(true));
        });
    }

    public void givePlayer(Player player, String key) {
        String[] keys = key.split(":");
        givePlayer(player, new NamespacedKey(keys[0], keys[1]));
    }

    public void givePlayer(Player player, NamespacedKey key) {
        if (itemStackMap.containsKey(key))
            player.getInventory().addItem(AsukaMeow
                    .INSTANCE
                    .getNMSManager()
                    .getItemFactory()
                    .toItemStack(itemStackMap.get(key))
            );
    }

}
