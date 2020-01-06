package at.meowww.AsukaMeow.item.feature.v1_14_R1;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class FeatureBinding extends at.meowww.AsukaMeow.item.feature.FeatureBinding {

    public FeatureBinding() {}

    public FeatureBinding(Type type) {
        super(type);
    }

    @Override
    public <T extends Event> void trigger(ItemStack itemStack, T event) {
        if (event instanceof PlayerDropItemEvent)
            onDrop(itemStack, (PlayerDropItemEvent) event);
        else if (event instanceof InventoryClickEvent)
            onInventoryClickSlot(itemStack, (InventoryClickEvent) event);
    }

    @Override
    public ItemStack serialize(ItemStack itemStack) {
        NBTTagCompound bindingCom = new NBTTagCompound();
        bindingCom.setString("type", type.toString());

        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tagCom = nmsStack.getOrCreateTag();

        NBTTagCompound featureCom = tagCom.getCompound("feature");
        featureCom.set(FeatureBinding.lowerName, bindingCom);
        if (!tagCom.hasKey("feature"))
            tagCom.set("feature", featureCom);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public at.meowww.AsukaMeow.item.feature.FeatureBinding deserialize(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound featureCom = nmsStack.getTag().getCompound("feature");
        NBTTagCompound bindingCom = featureCom.getCompound(FeatureBinding.lowerName);

        this.type = Type.valueOf(bindingCom.getString("type"));
        return this;
    }

    public static at.meowww.AsukaMeow.item.feature.FeatureBinding deserialize(JsonObject jsonObj) {
        return new FeatureBinding(
                Type.valueOf(jsonObj.get("type").getAsString().toUpperCase())
        );
    }

}
