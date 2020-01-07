package at.meowww.AsukaMeow.item.feature.v1_14_R1;

import at.meowww.AsukaMeow.util.Utils;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FeatureTeleport extends at.meowww.AsukaMeow.item.feature.FeatureTeleport {

    public FeatureTeleport() {}

    public FeatureTeleport(Location location, int cooldown, Date nextUseDatetime) {
        super(location, cooldown, nextUseDatetime);
    }

    @Override
    public ItemStack onInteract(ItemStack itemStack, PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR)
            return itemStack;

        Player player = event.getPlayer();
        if (new Date().after(nextUseDatetime)) {
            nextUseDatetime = DateUtils.addSeconds(new Date(), cooldown);
            player.teleport(location);
            player.sendActionBar("傳送 " + location);
        } else {
            player.sendActionBar("傳送失敗: 仍需冷卻 "
                    + Utils.getDatetimeDiffInt(new Date(), nextUseDatetime, TimeUnit.SECONDS)
                    + " 秒."
            );
        }
        itemStack = serialize(updateLore(itemStack));
        if (event.getHand().equals(EquipmentSlot.HAND))
            player.getInventory().setItemInMainHand(itemStack);
        else if (event.getHand().equals(EquipmentSlot.OFF_HAND))
            player.getInventory().setItemInOffHand(itemStack);
        return itemStack;
    }

    @Override
    public ItemStack onItemHeld(ItemStack itemStack, PlayerItemHeldEvent event) {
        itemStack = updateLore(itemStack);
        event.getPlayer().getInventory().setItem(event.getNewSlot(), itemStack);
        return itemStack;
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

    @Override
    public FeatureTeleport deserialize(ItemStack itemStack) {
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
