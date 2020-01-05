package at.meowww.AsukaMeow.nms;

import at.meowww.AsukaMeow.item.feature.IFeature;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public abstract class FeatureFactory {

    public abstract void trigger(ItemStack itemStack, Event event);

    public abstract JsonObject serialize(IFeature feature);

    public abstract ItemStack serialize(IFeature feature, ItemStack itemStack);

    public abstract IFeature deserialize(JsonElement jsonEle);

    public abstract IFeature manualDeserialize(String featureName, ItemStack itemStack);

}
