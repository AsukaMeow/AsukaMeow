package at.meowww.AsukaMeow.user;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.util.Utils;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class User {

    private UUID uuid;
    private String mojangName;
    private String displayName;
    private String hashedPassword;
    private Date registerDatetime;
    private Date lastLoginDatetime;
    private Date lastLogoutDatetime;
    private int totalOnlineSecond; // 10 years = 315,569,260 seconds
    private Set<String> historyLoginIP;
    private Date announceReadDate;

    private Location lastLogoutLoc;
    private UserInventory userInventory;

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
            Date announceReadDate,
            Location lastLogoutLoc,
            UserInventory userInventory
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
        this.announceReadDate = announceReadDate;

        this.lastLogoutLoc = lastLogoutLoc;
        this.userInventory = userInventory;
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
                new Date(),

                player.getLocation(),
                new UserInventory(player.getInventory())
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
                .append("announce_read_date", user.announceReadDate)

                .append("last_logout_location", user.lastLogoutLoc.serialize())
                .append("user_inventory", user.userInventory.serialize());
    }

    public static User fromDocument (Document document) {
        return new User(
                UUID.fromString(document.getString("uuid")),
                document.get("mojang_name", ""),
                document.get("display_name", ""),
                document.get("hashed_password", ""),
                document.get("register_datetime", new Date(0)),
                document.get("last_login_datetime", new Date(0)),
                document.get("last_logout_datetime", new Date(0)),
                document.getInteger("total_online_second", 0),
                new HashSet<>(document.get("history_login_ip", new ArrayList<>())),
                document.get("announce_read_date", new Date(0)),

                Location.deserialize(document.get(
                        "last_logout_location",
                        AsukaMeow.INSTANCE.getDefaultWorld().getSpawnLocation().serialize()
                        )
                ),
                UserInventory.deserialize(document.get("user_inventory", new HashMap<>()))
        );
    }

    public void online (Player player) {
        lastLoginDatetime = new Date();
        historyLoginIP.add(player.getAddress().getHostName());
        userInventory.presetInventory(player);
    }

    public void offline (Player player) {
        lastLogoutDatetime = new Date();
        totalOnlineSecond += Utils.getDatetimeDiffInt(lastLoginDatetime, lastLogoutDatetime, TimeUnit.SECONDS);
        lastLogoutLoc = player.getLocation();
        userInventory.postInventory(player);
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

    public Date getAnnounceReadDate () {
        return announceReadDate;
    }

    public void updateAnnounceReadDate () {
        this.announceReadDate = new Date();
    }

    public void setUserInventory (UserInventory userInventory) {
        this.userInventory = userInventory;
    }
}
