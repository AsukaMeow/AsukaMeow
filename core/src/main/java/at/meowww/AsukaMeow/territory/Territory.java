package at.meowww.AsukaMeow.territory;

import at.meowww.AsukaMeow.AsukaMeow;
import org.bson.Document;
import org.bukkit.Location;

import java.util.Map;

public class Territory {

    private String id;
    private String title;
    private String subtitle;
    private Location spawn;
    private Cuboid cuboid;

    public Territory(
            String id,
            String title,
            String subtitle,
            Location spawn,
            Cuboid cuboid
    ) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.spawn = spawn;
        this.cuboid = cuboid;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Location getSpawn () {
        return this.spawn;
    }

    public boolean isEnter(Location location) {
        if (!location.getWorld().equals(spawn.getWorld()))
            return false;
        return this.cuboid.isWithIn(location);
    }

    public static Document toDocument (Territory territory) {
        return new Document("id", territory.id)
                .append("title", territory.title)
                .append("subtitle", territory.subtitle)
                .append("spawn", territory.spawn.serialize())
                .append("cuboid", Cuboid.toDocument(territory.cuboid));
    }

    public static Territory fromDocument (Document document) {
        Map<String, Object> defaultWorld =
                AsukaMeow.INSTANCE.getDefaultWorld().getSpawnLocation().serialize();
        return new Territory(
                document.getString("id"),
                document.get("title", ""),
                document.get("subtitle", ""),
                Location.deserialize(document.get("spawn", defaultWorld)),
                Cuboid.fromDocument((Document) document.get("cuboid"))
        );
    }

}
