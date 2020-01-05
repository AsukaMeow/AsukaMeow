package at.meowww.AsukaMeow.item;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.item.feature.IFeature;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class AsukaItem {

    private NamespacedKey id;
    private ItemStack itemStack;
    private Set<IFeature> features;

    public AsukaItem(NamespacedKey id, ItemStack itemStack) {
        this(id, itemStack, new HashSet<>());
    }

    public AsukaItem(NamespacedKey id, ItemStack itemStack, Set<IFeature> features) {
        this.id = id;
        this.itemStack = itemStack;
        this.features = features;
        this.resetItemInfo();
    }

    public NamespacedKey getId() {
        return this.id;
    }

    public ItemStack getItemStack () {
        return this.itemStack;
    }

    public Set<IFeature> getFeatures () {
        return this.features;
    }

    public void resetItemInfo () {
        this.itemStack.getItemMeta().addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS
        );
        this.features.forEach(feature -> feature.updateLore(itemStack));
    }

    public static JsonArray featuresSerialize (AsukaItem asukaItem) {
        JsonArray featureArr = new JsonArray();
        asukaItem.features.forEach(feature -> featureArr.add(AsukaMeow.INSTANCE
                .getNMSManager()
                .getFeatureFactory()
                .serialize(feature)));
        return featureArr;
    }

    public static JsonObject serialize(AsukaItem asukaItem) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", asukaItem.id.toString());

        jsonObj.addProperty("item_stack", AsukaMeow
                .INSTANCE
                .getNMSManager()
                .getItemFactory()
                .serialize(asukaItem.itemStack));

        if (!asukaItem.features.isEmpty())
            jsonObj.add("feature", featuresSerialize(asukaItem));

        return jsonObj;
    }

    public static Set<IFeature> featuresDeserialize (JsonArray jsonArr) {
        Set<IFeature> features = new HashSet<>();
        jsonArr.forEach(ele -> features.add(AsukaMeow.INSTANCE
                .getNMSManager()
                .getFeatureFactory().deserialize(ele)));
        return features;
    }

    public static AsukaItem deserialize(JsonObject jsonObj) {
        String[] keys = jsonObj.get("id").getAsString().split(":");
        NamespacedKey key = new NamespacedKey(keys[0], keys[1]);

        ItemStack itemStack = AsukaMeow
                .INSTANCE
                .getNMSManager()
                .getItemFactory()
                .deserialize(jsonObj.get("item_stack").getAsString());

        Set<IFeature> features = new HashSet<>();
        if (jsonObj.has("feature"))
            features = featuresDeserialize(
                    jsonObj.get("feature").getAsJsonArray());

        return new AsukaItem(key, itemStack, features);
    }

}
