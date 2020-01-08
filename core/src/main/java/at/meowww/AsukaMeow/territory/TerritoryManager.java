package at.meowww.AsukaMeow.territory;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.database.IMongoStorable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class TerritoryManager implements IMongoStorable {

    private TerritoryListener listener;

    private MongoCollection territoryCol;
    private Map<String, Territory> territoryMap = new HashMap<>();

    public TerritoryManager () {
        this.listener = new TerritoryListener(this);
    }

    @Override
    public void initCollection(MongoDatabase db) {
        this.territoryCol = db.getCollection("territory");
    }

    public void registerListener () {
        Bukkit.getServer().getPluginManager().registerEvents(listener, AsukaMeow.INSTANCE);
    }

    public Territory enterTerritory(Location location) {
        for (Territory territory : territoryMap.values())
            if (territory.isEnter(location))
                return territory;
        return null;
    }

    public Map<String, Territory> getTerritoryMap () {
        return this.territoryMap;
    }

    public void loadTerritory(String id) {
        Object obj = territoryCol.find(new Document("id", id)).first();
        if (obj != null) {
            Territory territory = Territory.fromDocument((Document) obj);

            if (territoryMap.containsKey(id))
                territoryMap.replace(id, territory);
            else
                territoryMap.put(id, territory);
        }

    }

    public void loadTerritories() {
        Territory territory;
        Map<String, Territory> loadedTerritoryMap = new HashMap<>();
        MongoCursor<Document> cursor = territoryCol.find().cursor();
        while(cursor.hasNext()) {
            territory = Territory.fromDocument(cursor.next());
            loadedTerritoryMap.put(territory.getId(), territory);
            AsukaMeow.INSTANCE.getLogger().info("Territory ["
                    + territory.getTitle() + "/"
                    + territory.getId() + "] loaded!");
        }
        territoryMap = loadedTerritoryMap;
        AsukaMeow.INSTANCE.getLogger().info("Loaded " + territoryMap.size() + " territories!");
    }

}
