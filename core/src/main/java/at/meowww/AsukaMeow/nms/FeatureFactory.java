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
     * Examine the given ItemStack has `feature` NBTTagCompound in stack's
     *  tag.
     * @param itemStack
     * @return
     */
    public abstract boolean hasFeature(ItemStack itemStack);

    /**
     * Gateway method to pass any Bukkit event to feature.
     * The feature will implement proper event handler and pass in this method.
     * @param itemStack
     * @param event
     */
    public abstract void trigger(ItemStack itemStack, Event event);

    /**
     * This method will retain some feature's state value, and it decide by feature itself.
     *
     * @param feature The feature which its default value is going to set.
     * @param itemStack The ItemStack who is going to be reset its feature value.
     *                  Value which is going to retain is decide by feature's update method.
     *                  {@link IFeature#update(ItemStack)}
     * @return The ItemStack with completely reset default value of given feature.
     */
    public abstract ItemStack featureUpdate(IFeature feature, ItemStack itemStack);

    /**
     * Serialize a feature's data into JsonElement.
     * This method is call when a AsukaItem is going to be write to Database.
     * @param feature
     * @return
     */
    public abstract JsonObject serialize(IFeature feature);

    /**
     * Write the given feature's NBT data into given ItemStack, and output
     * the ItemStack.
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
     * This method allows deserialize a ItemStack's NBT tag with given Feature name.
     * If given {@param itemStackitem} stack's NBT tag contains with {@param featureName},
     *  will return the deserialize result, otherwise null.
     * @param featureName
     * @param itemStack
     * @return
     */
    public abstract IFeature manualDeserialize(String featureName, ItemStack itemStack);

}