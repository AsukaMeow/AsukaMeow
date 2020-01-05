package at.meowww.AsukaMeow.item.feature;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.item.AsukaItem;
import at.meowww.AsukaMeow.util.Utils;
import com.google.gson.JsonObject;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FeatureTeleport extends Feature {

    public static final String name ="TELEPORT";
    private Location location;
    private int cooldown;
    private Date nextUseDatetime;

    public FeatureTeleport(Location location) {
        this(location, -1, new Date(0));
    }

    public FeatureTeleport(Location location, int cooldown) {
        this(location, cooldown, new Date(0));
    }

    public FeatureTeleport(Location location, int cooldown, Date nextUseDatetime) {
        this.location = location;
        this.cooldown = cooldown;
        this.nextUseDatetime = nextUseDatetime;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T extends Event> void trigger(AsukaItem item, T event) {
        if (event instanceof  PlayerInteractEvent) {
            PlayerInteractEvent pie = (PlayerInteractEvent) event;
            if (new Date().after(nextUseDatetime)) {
                nextUseDatetime = DateUtils.addSeconds(new Date(), cooldown);
                pie.getPlayer().teleport(location);
                pie.getPlayer().sendActionBar("傳送 " + location);
            } else {
                pie.getPlayer().sendActionBar("傳送失敗: 仍需冷卻 "
                        + Utils.getDatetimeDiffInt(new Date(), nextUseDatetime, TimeUnit.SECONDS)
                        + " 秒."
                );
            }
            pie.getPlayer().getInventory().setItemInMainHand(
                    AsukaMeow.INSTANCE
                            .getNMSManager()
                            .getItemFactory()
                            .setAsukaItem(item)
            );
        }
    }

    @Override
    public <T extends Event> void updateLore(AsukaItem item, T event) {
        updateLore(item);
    }

    @Override
    public void updateLore(AsukaItem item) {
        if (new Date().before(nextUseDatetime)) {
            String leftString = "剩餘冷卻時間："
                    + Utils.getDatetimeDiffInt(
                            new Date(), nextUseDatetime, TimeUnit.SECONDS)
                    + "秒";
            List<String> lores = item.getItemStack().getLore();
            for(int i = 0; i < lores.size(); ++i) {
                if (lores.get(i).contains("剩餘冷卻時間：")) {
                    lores.set(i, leftString);
                    item.getItemStack().setLore(lores);
                    return;
                }
            }
            lores.add(0, leftString);
            item.getItemStack().setLore(lores);
        }
    }

    public static JsonObject serialize(Feature feat) {
        FeatureTeleport feature = (FeatureTeleport) feat;
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", feature.getName());

        JsonObject locObj = new JsonObject();
        locObj.addProperty("world", feature.location.getWorld().getName());
        locObj.addProperty("x", feature.location.getX());
        locObj.addProperty("y", feature.location.getY());
        locObj.addProperty("z", feature.location.getZ());
        locObj.addProperty("yaw", feature.location.getYaw());
        locObj.addProperty("pitch", feature.location.getPitch());
        jsonObj.add("location", locObj);

        jsonObj.addProperty("cooldown", feature.cooldown);
        jsonObj.addProperty("next_use_datetime", feature.nextUseDatetime.getTime());
        return jsonObj;
    }

    public static FeatureTeleport deserialize(JsonObject jsonObj) {
        JsonObject locObj = jsonObj.get("location").getAsJsonObject();
        Map<String, Object> locMap = new HashMap<>();
        locMap.put("world", locObj.get("world").getAsString());
        locMap.put("x", locObj.get("x").getAsDouble());
        locMap.put("y", locObj.get("y").getAsDouble());
        locMap.put("z", locObj.get("z").getAsDouble());
        locMap.put("yaw", locObj.get("yaw").getAsFloat());
        locMap.put("pitch", locObj.get("pitch").getAsFloat());

        Date nextUseDatetime = new Date(0);
        if (jsonObj.has("next_use_datetime"))
            nextUseDatetime = new Date(jsonObj.get("next_use_datetime").getAsLong());

        return new FeatureTeleport(
                Location.deserialize(locMap),
                jsonObj.get("cooldown").getAsInt(),
                nextUseDatetime
        );
    }
}
