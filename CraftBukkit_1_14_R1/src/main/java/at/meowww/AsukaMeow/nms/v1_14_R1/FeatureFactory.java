package at.meowww.AsukaMeow.nms.v1_14_R1;

import at.meowww.AsukaMeow.item.feature.IFeature;
import at.meowww.AsukaMeow.item.feature.v1_14_R1.FeatureBinding;
import at.meowww.AsukaMeow.item.feature.v1_14_R1.FeatureTeleport;
import at.meowww.AsukaMeow.item.feature.v1_14_R1.FeatureTime;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class FeatureFactory extends at.meowww.AsukaMeow.nms.FeatureFactory {

    @Override
    public boolean hasFeature(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        if (!nmsStack.hasTag())
            return false;
        return nmsStack.getTag().hasKey("feature");
    }

    @Override
    public void trigger(ItemStack itemStack, Event event) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound featureCom = nmsStack.getTag().getCompound("feature");
        if (featureCom.hasKey(FeatureTeleport.lowerName))
            itemStack = new FeatureTeleport().deserialize(itemStack).trigger(itemStack, event);
        if (featureCom.hasKey(FeatureBinding.lowerName))
            itemStack = new FeatureBinding().deserialize(itemStack).trigger(itemStack, event);
        if (featureCom.hasKey(FeatureTime.lowerName))
            itemStack = new FeatureTime().deserialize(itemStack).trigger(itemStack, event);
    }

    @Override
    public ItemStack resetVanillaItemStackLore(ItemStack itemStack) {
        if (hasFeature(itemStack)) {
            net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                    CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound featureCom = nmsStack.getTag().getCompound("feature");
            if (featureCom.hasKey(FeatureTeleport.lowerName))
                itemStack = new FeatureTeleport().deserialize(itemStack).resetLore(itemStack);
            if (featureCom.hasKey(FeatureBinding.lowerName))
                itemStack = new FeatureBinding().deserialize(itemStack).resetLore(itemStack);
            if (featureCom.hasKey(FeatureTime.lowerName))
                itemStack = new FeatureTime().deserialize(itemStack).resetLore(itemStack);
        }
        return itemStack;
    }

    @Override
    public IFeature deserialize(JsonElement jsonEle) {
        JsonObject jsonObj = jsonEle.getAsJsonObject();
        switch (jsonObj.get("name").getAsString().toUpperCase()) {
            case FeatureTeleport.name:
                return FeatureTeleport.deserialize(jsonObj);
            case FeatureBinding.name:
                return FeatureBinding.deserialize(jsonObj);
            case FeatureTime.name:
                return FeatureTime.deserialize(jsonObj);
        }
        return null;
    }

    @Override
    public IFeature manualDeserialize(String featureName, ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound featureCom = nmsStack.getTag().getCompound("feature");
        if (featureName.equalsIgnoreCase(FeatureTeleport.lowerName) && featureCom.hasKey(FeatureTeleport.lowerName))
            return new FeatureTeleport().deserialize(itemStack);
        else if (featureName.equalsIgnoreCase(FeatureBinding.lowerName) && featureCom.hasKey(FeatureBinding.lowerName))
            return new FeatureBinding().deserialize(itemStack);
        else if (featureName.equalsIgnoreCase(FeatureTime.lowerName) && featureCom.hasKey(FeatureTime.lowerName))
            return new FeatureTime().deserialize(itemStack);
        return null;
    }

}
