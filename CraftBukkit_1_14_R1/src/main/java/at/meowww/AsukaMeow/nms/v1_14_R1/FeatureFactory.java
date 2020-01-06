package at.meowww.AsukaMeow.nms.v1_14_R1;

import at.meowww.AsukaMeow.item.feature.IFeature;
import at.meowww.AsukaMeow.item.feature.v1_14_R1.FeatureBinding;
import at.meowww.AsukaMeow.item.feature.v1_14_R1.FeatureTeleport;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class FeatureFactory extends at.meowww.AsukaMeow.nms.FeatureFactory {

    public void trigger(ItemStack itemStack, Event event) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound featureCom = nmsStack.getTag().getCompound("feature");
        if (featureCom.hasKey(FeatureTeleport.lowerName))
            new FeatureTeleport().deserialize(itemStack).trigger(itemStack, event);
        if (featureCom.hasKey(FeatureBinding.lowerName))
            new FeatureBinding().deserialize(itemStack).trigger(itemStack, event);
    }

    @Override
    public JsonObject serialize(IFeature feature) {
        if (feature instanceof FeatureTeleport)
            return FeatureTeleport.serialize(feature);
        else if (feature instanceof FeatureBinding)
            return FeatureBinding.serialize(feature);
        return null;
    }

    @Override
    public ItemStack serialize(IFeature feature, ItemStack itemStack) {
        if (feature instanceof FeatureTeleport)
            return feature.serialize(itemStack);
        else if (feature instanceof FeatureBinding)
            return feature.serialize(itemStack);
        return null;
    }

    @Override
    public IFeature deserialize(JsonElement jsonEle) {
        JsonObject jsonObj = jsonEle.getAsJsonObject();
        switch (jsonObj.get("name").getAsString().toUpperCase()) {
            case FeatureTeleport.name:
                return FeatureTeleport.deserialize(jsonObj);
            case FeatureBinding.name:
                return FeatureBinding.deserialize(jsonObj);
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
        return null;
    }

}
