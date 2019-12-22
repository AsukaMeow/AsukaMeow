package at.meowww.AsukaMeow.user;

import at.meowww.AsukaMeow.util.Utils;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class User extends BasicDBObject {

    private UUID uuid;
    private String mojangName;
    private String displayName;
    private String hashedPassword;
    private Date registerDatetime;
    private Date lastLoginDatetime;
    private Date lastLogoutDatetime;
    private Location lastLogoutLoc;

    private int totalOnlineSecond; // 10 years = 315,569,260 seconds

    public User(UUID uuid, String mojangName, String displayName, String hashedPassword, Date registerDatetime, Date lastLoginDatetime, Date lastLogoutDatetime, Location lastLogoutLoc, int totalOnlineSecond) {
        this.uuid = uuid;
        this.mojangName = mojangName;
        this.displayName = displayName;
        this.hashedPassword = hashedPassword;
        this.registerDatetime = registerDatetime;
        this.lastLoginDatetime = lastLoginDatetime;
        this.lastLogoutDatetime = lastLogoutDatetime;
        this.lastLogoutLoc = lastLogoutLoc;
        this.totalOnlineSecond = totalOnlineSecond;
    }

    public static User newUser (Player player) {
        return new User(
                player.getUniqueId(),
                player.getName(),
                player.getDisplayName(),
                "UNPASSWORD",
                new Date(),
                null,
                null,
                player.getLocation(),
                0);
    }

    public static Document toDocument (User user) {
        return new Document("uuid", user.uuid.toString())
                .append("mojang_name", user.mojangName)
                .append("display_name", user.displayName)
                .append("hashed_password", user.hashedPassword)
                .append("register_datetime", user.registerDatetime)
                .append("last_login_datetime", user.lastLoginDatetime)
                .append("last_logout_datetime", user.lastLogoutDatetime)
                .append("last_logout_location", user.lastLogoutLoc.serialize())
                .append("total_online_second", user.totalOnlineSecond);
    }

    public static User fromDocument (Document document) {
        return new User(UUID.fromString(document.getString("uuid")),
                document.getString("mojang_name"),
                document.getString("display_name"),
                document.getString("hashed_password"),
                document.getDate("register_datetime"),
                document.getDate("last_login_datetime"),
                document.getDate("last_logout_datetime"),
                Location.deserialize((Map<String, Object>)document.get("last_logout_location")),
                document.getInteger("total_online_second", 0)
        );
    }

    public void online () {
        lastLoginDatetime = new Date();
    }

    public void offline (Location loc) {
        lastLogoutDatetime = new Date();
        totalOnlineSecond += Utils.getDatetimeDiffInt(lastLoginDatetime, lastLogoutDatetime, TimeUnit.SECONDS);
        lastLogoutLoc = loc;
    }

    public UUID getUUID () {
        return uuid;
    }

    public String getMojangName () {
        return this.mojangName;
    }

    public String getDisplayName () {
        return displayName;
    }

    public String getHashedPassword () {
        return hashedPassword;
    }

    public Date getRegisterDatetime () {
        return registerDatetime;
    }

    public Date getLastLoginDatetime () {
        return lastLoginDatetime;
    }

    public Date getLastLogoutDatetime () {
        return lastLogoutDatetime;
    }

    public Location getLastLogoutLoc () {
        return lastLogoutLoc;
    }

}
