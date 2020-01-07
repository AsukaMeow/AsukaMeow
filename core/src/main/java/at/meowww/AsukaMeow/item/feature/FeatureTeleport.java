package at.meowww.AsukaMeow.item.feature;

import at.meowww.AsukaMeow.util.Utils;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class FeatureTeleport implements IFeature {

    public static final String name ="TELEPORT";
    public static final String lowerName = name.toLowerCase();
    protected Location location;
    protected int cooldown;
    protected Date nextUseDatetime;

    public FeatureTeleport () {}

    public FeatureTeleport(Location location, int cooldown, Date nextUseDatetime) {
        this.location = location;
        this.cooldown = cooldown;
        this.nextUseDatetime = nextUseDatetime;
    }

    @Override
    public <T extends Event> ItemStack trigger(ItemStack itemStack, T event) {
        if (event instanceof PlayerInteractEvent)
            itemStack = onInteract(itemStack, (PlayerInteractEvent) event);
        else if (event instanceof PlayerItemHeldEvent)
            itemStack = onItemHeld(itemStack, (PlayerItemHeldEvent) event);
        return itemStack;
    }

    @Override
    public <T extends Event> ItemStack updateLore(ItemStack item, T event) {
        return updateLore(item);
    }

    @Override
    public ItemStack updateLore(ItemStack itemStack) {
        if (new Date().before(nextUseDatetime)) {
            String leftString = "剩餘冷卻時間："
                    + Utils.getDatetimeDiffInt(
                            new Date(), nextUseDatetime, TimeUnit.SECONDS)
                    + "秒";
            List<String> lores = itemStack.getLore();
            for(int i = 0; i < lores.size(); ++i) {
                if (lores.get(i).contains("剩餘冷卻時間：")) {
                    lores.set(i, leftString);
                    itemStack.setLore(lores);
                    return itemStack;
                }
            }
            lores.add(0, leftString);
            itemStack.setLore(lores);
            return itemStack;
        } else {
            return resetLore(itemStack);
        }
    }

    @Override
    public ItemStack resetLore(ItemStack itemStack) {
        List<String> lores = itemStack.getLore();
        for (int i = 0; i < lores.size(); ++i) {
            if (lores.get(i).contains("剩餘冷卻時間")) {
                lores.remove(i);
                itemStack.setLore(lores);
                return itemStack;
            }
        }
        return itemStack;
    }

    @Override
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

    public abstract ItemStack onInteract(ItemStack itemStack, PlayerInteractEvent event);

    public abstract ItemStack onItemHeld(ItemStack itemStack, PlayerItemHeldEvent event);

}
