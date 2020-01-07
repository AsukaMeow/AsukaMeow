package at.meowww.AsukaMeow.territory;

import org.bson.Document;
import org.bukkit.Location;

public class Cuboid {

    private int xA, yA, zA, xB, yB, zB;

    public Cuboid(int xA, int yA, int zA, int xB, int yB, int zB) {
        this.xA = xA;
        this.yA = yA;
        this.zA = zA;
        this.xB = xB;
        this.yB = yB;
        this.zB = zB;
    }

    public boolean isWithIn(int x, int y, int z) {
        if (x < Math.min(xA, xB) || x > Math.max(xA, xB))
            return false;
        else if (y < Math.min(yA, yB) || y > Math.max(yA, yB))
            return false;
        else if (z < Math.min(zA, zB) || z > Math.max(zA, zB))
            return false;
        return true;
    }

    public boolean isWithIn(Location location) {
        return isWithIn(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Document toDocument (Cuboid cuboid) {
        return new Document("x_a", cuboid.xA)
                        .append("y_a", cuboid.yA)
                        .append("z_a", cuboid.zA)
                        .append("x_b", cuboid.xB)
                        .append("y_b", cuboid.yB)
                        .append("z_b", cuboid.zB);
    }

    public static Cuboid fromDocument (Document document) {
        return new Cuboid(
                document.getInteger("x_a"),
                document.getInteger("y_a"),
                document.getInteger("z_a"),
                document.getInteger("x_b"),
                document.getInteger("y_b"),
                document.getInteger("z_b")
        );
    }

}
