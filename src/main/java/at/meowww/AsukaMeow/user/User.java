package at.meowww.AsukaMeow.user;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.util.Utils;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class User extends BasicDBObject {

    private UUID uuid;
    private String mojangName;
    private String displayName;
    private String hashedPassword;
    private Date registerDatetime;
    private Date lastLoginDatetime;
    private Date lastLogoutDatetime;
    private int totalOnlineSecond; // 10 years = 315,569,260 seconds
    private Set<String> historyLoginIP;

    private Location lastLogoutLoc;

    public User(
            UUID uuid,
            String mojangName,
            String displayName,
            String hashedPassword,
            Date registerDatetime,
            Date lastLoginDatetime,
            Date lastLogoutDatetime,
            int totalOnlineSecond,
            Set<String> historyLoginIP,
            Location lastLogoutLoc
    ) {
        this.uuid = uuid;
        this.mojangName = mojangName;
        this.displayName = displayName;
        this.hashedPassword = hashedPassword;
        this.registerDatetime = registerDatetime;
        this.lastLoginDatetime = lastLoginDatetime;
        this.lastLogoutDatetime = lastLogoutDatetime;
        this.totalOnlineSecond = totalOnlineSecond;
        this.historyLoginIP = historyLoginIP;

        this.lastLogoutLoc = lastLogoutLoc;
    }

    public static User newUser (Player player) {
        return new User(
                player.getUniqueId(),
                player.getName(),
                player.getDisplayName(),
                "UNPASSWORD",
                new Date(),
                new Date(),
                new Date(),
                0,
                new HashSet<>(),

                player.getLocation()
        );
    }

    public static Document toDocument (User user) {
        return new Document("uuid", user.uuid.toString())
                .append("mojang_name", user.mojangName)
                .append("display_name", user.displayName)
                .append("hashed_password", user.hashedPassword)
                .append("register_datetime", user.registerDatetime)
                .append("last_login_datetime", user.lastLoginDatetime)
                .append("last_logout_datetime", user.lastLogoutDatetime)
                .append("total_online_second", user.totalOnlineSecond)
                .append("history_login_ip", new ArrayList<>(user.historyLoginIP))

                .append("last_logout_location", user.lastLogoutLoc.serialize());
    }

    public static User fromDocument (Document document) {
        return new User(
                UUID.fromString(document.getString("uuid")),
                document.get("mojang_name", ""),
                document.get("display_name", ""),
                document.get("hashed_password", ""),
                document.get("register_datetime", new Date()),
                document.get("last_login_datetime", new Date()),
                document.get("last_logout_datetime", new Date()),
                document.getInteger("total_online_second", 0),
                new HashSet<>(document.get("history_login_ip", new ArrayList<>())),

                Location.deserialize(document.get(
                        "last_logout_location",
                        AsukaMeow.INSTANCE.getDefaultWorld().getSpawnLocation().serialize()
                        )
                )
        );
    }

    public void online (Player player) {
        lastLoginDatetime = new Date();
        historyLoginIP.add(player.getAddress().getHostName());
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
