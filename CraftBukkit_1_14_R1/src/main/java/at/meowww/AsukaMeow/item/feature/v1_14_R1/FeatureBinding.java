package at.meowww.AsukaMeow.item.feature.v1_14_R1;

import com.google.gson.JsonObject;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FeatureBinding extends at.meowww.AsukaMeow.item.feature.FeatureBinding {

    public FeatureBinding() {}

    public FeatureBinding(Type type) {
        super(type);
    }

    @Override
    public void onDrop(ItemStack item, PlayerDropItemEvent event) {
        if (type == Type.UNDROPABLE) {
            event.getPlayer().sendActionBar("靈魂綁定的物品不能被丟棄");
            event.setCancelled(true);
        }
    }

    @Override
    public void onInventoryClickSlot(ItemStack item, InventoryClickEvent event) {
        if (type == Type.UNDROPABLE) {
            Event.Result result = Event.Result.ALLOW;
            if (event.isShiftClick())
                result = event.getInventory().getType().equals(InventoryType.CRAFTING)
                        ? Event.Result.ALLOW : Event.Result.DENY;
            else if (!(event.getClickedInventory() instanceof PlayerInventory))
                result = Event.Result.DENY;
            event.setResult(result);
        }
    }

    @Override
    public ItemStack update(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound bindingCom = nmsStack.getTag()
                .getCompound("feature")
                .getCompound(FeatureBinding.lowerName);

        bindingCom.setString("type", type.toString());

        return CraftItemStack.asBukkitCopy(nmsStack);
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
    public FeatureBinding deserialize(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound featureCom = nmsStack.getTag().getCompound("feature");
        NBTTagCompound bindingCom = featureCom.getCompound(FeatureBinding.lowerName);

        this.type = Type.valueOf(bindingCom.getString("type"));
        return this;
    }

    public static FeatureBinding deserialize(JsonObject jsonObj) {
        return new FeatureBinding(
                Type.valueOf(jsonObj.get("type").getAsString().toUpperCase())
        );
    }

}
