package at.meowww.AsukaMeow.item.feature.v1_14_R1;

import at.meowww.AsukaMeow.item.feature.IFeature;
import at.meowww.AsukaMeow.util.Utils;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FeatureTeleport extends at.meowww.AsukaMeow.item.feature.FeatureTeleport {

    public FeatureTeleport() {}

    public FeatureTeleport(Location location, int cooldown, Date nextUseDatetime) {
        super(location, cooldown, nextUseDatetime);
    }

    @Override
    public <T extends Event> void trigger(ItemStack itemStack, T event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent pie = (PlayerInteractEvent) event;
            if (pie.getAction() != Action.RIGHT_CLICK_AIR)
                return;

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
            pie.getPlayer().getInventory().setItemInMainHand(serialize(itemStack));
        }
    }

    @Override
    public ItemStack update(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound teleportCom = nmsStack.getTag()
                .getCompound("feature")
                .getCompound(FeatureTeleport.lowerName);

        NBTTagCompound locCom = teleportCom.getCompound("location");
        locCom.setString("world", location.getWorld().getName());
        locCom.setDouble("x", location.getX());
        locCom.setDouble("y", location.getY());
        locCom.setDouble("z", location.getZ());
        locCom.setFloat("yaw", location.getYaw());
        locCom.setFloat("pitch", location.getPitch());

        teleportCom.setInt("cooldown", cooldown);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public ItemStack serialize(ItemStack itemStack) {
        NBTTagCompound locCom = new NBTTagCompound();
        locCom.setString("world", location.getWorld().getName());
        locCom.setDouble("x", location.getX());
        locCom.setDouble("y", location.getY());
        locCom.setDouble("z", location.getZ());
        locCom.setFloat("yaw", location.getYaw());
        locCom.setFloat("pitch", location.getPitch());

        NBTTagCompound teleportCom = new NBTTagCompound();
        teleportCom.set("location", locCom);
        teleportCom.setInt("cooldown", cooldown);
        teleportCom.setLong("next_use_datetime", nextUseDatetime.getTime());

        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tagCom = nmsStack.getOrCreateTag();

        NBTTagCompound featureCom = tagCom.getCompound("feature");
        featureCom.set(FeatureTeleport.lowerName, teleportCom);
        if (!tagCom.hasKey("feature"))
            tagCom.set("feature", featureCom);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public int hashCode() {
        return Objects.hash(location, cooldown);
    }

    public static JsonObject serialize(IFeature feat) {
        FeatureTeleport feature = (FeatureTeleport) feat;
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", FeatureTeleport.name);

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

    @Override
    public at.meowww.AsukaMeow.item.feature.FeatureTeleport deserialize(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound featureCom = nmsStack.getTag().getCompound("feature");
        NBTTagCompound teleportCom = featureCom.getCompound(FeatureTeleport.lowerName);
        NBTTagCompound locCom = teleportCom.getCompound("location");
        Map<String, Object> locMap = new HashMap<>();
        locMap.put("world", locCom.getString("world"));
        locMap.put("x", locCom.getDouble("x"));
        locMap.put("y", locCom.getDouble("y"));
        locMap.put("z", locCom.getDouble("z"));
        locMap.put("yaw", locCom.getFloat("yaw"));
        locMap.put("pitch", locCom.getFloat("pitch"));
        this.location = Location.deserialize(locMap);

        Date nextUseDatetime = new Date(0);
        if (teleportCom.hasKey("next_use_datetime"))
            nextUseDatetime = new Date(teleportCom.getLong("next_use_datetime"));
        this.nextUseDatetime = nextUseDatetime;

        this.cooldown = teleportCom.getInt("cooldown");
        return this;
    }

    public static at.meowww.AsukaMeow.item.feature.FeatureTeleport deserialize(JsonObject jsonObj) {
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
