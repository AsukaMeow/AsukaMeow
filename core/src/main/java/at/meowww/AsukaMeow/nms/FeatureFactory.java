package at.meowww.AsukaMeow.nms;

import at.meowww.AsukaMeow.item.feature.IFeature;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * Use to interact with Features.
 * @author clooooode
 * @since 0.0.5-SNAPSHOT
 */
public abstract class FeatureFactory {

    /**
     * Gateway method to pass any Bukkit event to feature.
     * The feature will implement proper event handler and pass in this method.
     * @param itemStack
     * @param event
     */
    public abstract void trigger(ItemStack itemStack, Event event);

    /**
     * Serialize a feature's data into JsonElement.
     * This method is call when a AsukaItem is going to be write to Database.
     * @param feature
     * @return
     */
    public abstract JsonObject serialize(IFeature feature);

    /**
     * Write the given feature's NBT data into given item stack, and output
     * the item stack.
     * @param feature
     * @param itemStack
     * @return
     */
    public abstract ItemStack serialize(IFeature feature, ItemStack itemStack);

    /**
     * Deserialize a JsonElement into Feature.
     * This method is call when a AsukaItem is going to be read from Database.
     * @param jsonEle
     * @return
     */
    public abstract IFeature deserialize(JsonElement jsonEle);

    /**
     * This method allows deserialize a item stack's NBT tag with given Feature name.
     * If given {@param itemStackitem} stack's NBT tag contains with {@param featureName},
     *  will return the deserialize result, otherwise null.
     * @param featureName
     * @param itemStack
     * @return
     */
    public abstract IFeature manualDeserialize(String featureName, ItemStack itemStack);

}
