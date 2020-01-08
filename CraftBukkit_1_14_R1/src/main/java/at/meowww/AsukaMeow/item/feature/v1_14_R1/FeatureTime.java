package at.meowww.AsukaMeow.item.feature.v1_14_R1;

import at.meowww.AsukaMeow.AsukaMeow;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class FeatureTime extends at.meowww.AsukaMeow.item.feature.FeatureTime {

    public FeatureTime() {}

    public FeatureTime(ItemStack targetItemStack, NamespacedKey targetAsukaItem, Date dueDatetime) {
        super(targetItemStack, targetAsukaItem, dueDatetime);
    }

    @Override
    public ItemStack examineReplace(Player player, int slotIndex) {
        ItemStack finalStack = player.getInventory().getItem(slotIndex);
        if (new Date().after(dueDatetime)) {
            if (targetItemStack != null)
                finalStack = targetItemStack;
            else if (targetAsukaItem != null)
                finalStack = AsukaMeow.INSTANCE.getNMSManager().getItemFactory().toItemStack(
                        AsukaMeow.INSTANCE.getItemManager().getAsukaItem(targetAsukaItem)
                );
            else
                finalStack = new ItemStack(Material.AIR);
        } else
            updateLore(finalStack);
        player.getInventory().setItem(slotIndex, finalStack);
        return finalStack;
    }

    @Override
    public ItemStack onItemHeld(ItemStack itemStack, PlayerItemHeldEvent event) {
        return examineReplace(event.getPlayer(), event.getNewSlot());
    }

    @Override
    public ItemStack onInteract(ItemStack itemStack, PlayerInteractEvent event) {
        itemStack = updateLore(itemStack);
        if (event.getHand().equals(EquipmentSlot.HAND))
            event.getPlayer().getInventory().setItemInMainHand(itemStack);
        else if (event.getHand().equals(EquipmentSlot.OFF_HAND))
            event.getPlayer().getInventory().setItemInOffHand(itemStack);
        return itemStack;
    }

    @Override
    public ItemStack update(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound timeCom = nmsStack.getTag()
                .getCompound("feature")
                .getCompound(FeatureTime.lowerName);
        timeCom.setLong("due_datetime", dueDatetime.getTime());
        timeCom.remove("target_item_stack");
        timeCom.remove("target_asuka_item");

        if (targetItemStack != null)
            timeCom.setString(
                    "target_item_stack",
                    AsukaMeow.INSTANCE
                            .getNMSManager()
                            .getItemFactory()
                            .serialize(targetItemStack)
                            // Minecraft can not process structured string,
                            // need to replace with \" to \\\" manually. Suck Minecraft!
                            .replaceAll("\"", "\\\""));
        if (targetAsukaItem != null)
            timeCom.setString("target_asuka_item", targetAsukaItem.toString());

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public ItemStack serialize(ItemStack itemStack) {
        NBTTagCompound timeCom = new NBTTagCompound();
        timeCom.setLong("due_datetime", dueDatetime.getTime());
        timeCom.remove("target_item_stack");
        timeCom.remove("target_asuka_item");

        if (targetItemStack != null)
            timeCom.setString(
                    "target_item_stack",
                    AsukaMeow.INSTANCE
                            .getNMSManager()
                            .getItemFactory()
                            .serialize(targetItemStack)
                            // Minecraft can not process structured string,
                            // need to replace with \" to \\\" manually. Suck Minecraft!
                            .replaceAll("\"", "\\\""));
        if (targetAsukaItem != null)
            timeCom.setString("target_asuka_item", targetAsukaItem.toString());

        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tagCom = nmsStack.getOrCreateTag();

        NBTTagCompound featureCom = tagCom.getCompound("feature");
        featureCom.set(FeatureTime.lowerName, timeCom);
        if (!tagCom.hasKey("feature"))
            tagCom.set("feature", featureCom);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public FeatureTime deserialize(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound featureCom = nmsStack.getTag().getCompound("feature");
        NBTTagCompound timeCom = featureCom.getCompound(FeatureTime.lowerName);

        ItemStack targetItemStack = null;
        NamespacedKey targetAsukaItem = null;
        if (timeCom.hasKey("target_item_stack"))
            targetItemStack = AsukaMeow.INSTANCE
                    .getNMSManager()
                    .getItemFactory()
                    .deserialize(timeCom.getString("target_item_stack"));
        if (timeCom.hasKey("target_asuka_item")) {
            String[] keys = timeCom.getString("target_asuka_item").split(":");
            targetAsukaItem = new NamespacedKey(keys[0], keys[1]);
        }

        this.targetItemStack = targetItemStack;
        this.targetAsukaItem = targetAsukaItem;
        this.dueDatetime = new Date(timeCom.getLong("due_datetime"));
        return this;
    }

    public static FeatureTime deserialize(JsonObject jsonObj) {
        ItemStack targetItemStack = null;
        NamespacedKey targetAsukaItemKey = null;
        if (jsonObj.has("target_item_stack"))
            targetItemStack = AsukaMeow.INSTANCE
                    .getNMSManager()
                    .getItemFactory()
                    .deserialize(jsonObj.get("target_item_stack").getAsString());
        if (jsonObj.has("target_asuka_item")){
            String[] keys = jsonObj.get("target_asuka_item").getAsString().split(":");
            targetAsukaItemKey = new NamespacedKey(keys[0], keys[1]);
        }
        return new FeatureTime(
                targetItemStack,
                targetAsukaItemKey,
                // Get long from second to millisecond.
                new Date(jsonObj.get("due_datetime").getAsLong() * 1000));
    }
}
