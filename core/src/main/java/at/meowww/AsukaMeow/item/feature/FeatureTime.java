package at.meowww.AsukaMeow.item.feature;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.util.Utils;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class FeatureTime implements IFeature {

    public static final String name = "TIME";
    public static final String lowerName = name.toLowerCase();
    protected ItemStack targetItemStack;
    protected NamespacedKey targetAsukaItem;
    protected Date dueDatetime;

    public FeatureTime() {}

    public FeatureTime(ItemStack targetItemStack, NamespacedKey targetAsukaItem, Date dueDatetime) {
        this.targetItemStack = targetItemStack;
        this.targetAsukaItem = targetAsukaItem;
        this.dueDatetime = dueDatetime;
    }

    public Date getDueDatetime () {
        return this.dueDatetime;
    }

    public abstract ItemStack examineReplace(Player player, int slotIndex);

    @Override
    public <T extends Event> ItemStack trigger(ItemStack itemStack, T event) {
        if (event instanceof  PlayerItemHeldEvent)
            itemStack = onItemHeld(itemStack, (PlayerItemHeldEvent) event);

        if (event instanceof PlayerInteractEvent)
            itemStack = onInteract(itemStack, (PlayerInteractEvent) event);

        return itemStack;
    }

    @Override
    public <T extends Event> ItemStack updateLore(ItemStack item, T event) {
        return updateLore(item);
    }

    @Override
    public ItemStack updateLore(ItemStack itemStack) {
        if (new Date().before(dueDatetime)) {
            String leftString = Utils.getDatetimeDiffInt(
                    new Date(), dueDatetime, TimeUnit.SECONDS)
                    + "秒後會轉化";
            if (targetItemStack != null)
                leftString += "為" + targetItemStack.getItemMeta().getDisplayName();
            else if (targetAsukaItem != null)
                leftString += "為" + AsukaMeow.INSTANCE
                        .getItemManager()
                        .getAsukaItem(targetAsukaItem)
                        .getItemStack()
                        .getItemMeta()
                        .getDisplayName();
            else
                leftString += "消失";

            List<String> lores = itemStack.getLore();
            for (int i = 0; i < lores.size(); ++i) {
                if (lores.get(i).contains("秒後會轉化")) {
                    lores.set(i, leftString);
                    itemStack.setLore(lores);
                    return itemStack;
                }
            }
            lores.add(0, leftString);
            itemStack.setLore(lores);
        }
        return itemStack;
    }

    @Override
    public ItemStack resetLore(ItemStack itemStack) {
        List<String> lores = itemStack.getLore();
        for (int i = 0; i < lores.size(); ++i) {
            if (lores.get(i).contains("秒後會轉化")) {
                lores.remove(i);
                itemStack.setLore(lores);
                return itemStack;
            }
        }
        return itemStack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetItemStack, targetAsukaItem, dueDatetime);
    }

    public static JsonObject serialize(IFeature feat) {
        FeatureTime feature = (FeatureTime) feat;
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", FeatureTime.name);

        if (feature.targetItemStack != null)
            jsonObj.addProperty("target_item_stack", AsukaMeow
                    .INSTANCE
                    .getNMSManager()
                    .getItemFactory()
                    .serialize(feature.targetItemStack));
        if (feature.targetAsukaItem != null)
            jsonObj.addProperty("target_asuka_item", feature.targetAsukaItem.toString());

        jsonObj.addProperty("due_datetime", feature.dueDatetime.getTime() / 1000);
        return jsonObj;
    }

    public abstract ItemStack onItemHeld(ItemStack itemStack, PlayerItemHeldEvent event);

    public abstract ItemStack onInteract(ItemStack itemStack, PlayerInteractEvent event);

}
