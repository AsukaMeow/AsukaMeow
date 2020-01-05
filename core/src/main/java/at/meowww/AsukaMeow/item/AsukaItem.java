package at.meowww.AsukaMeow.item;

import at.meowww.AsukaMeow.AsukaMeow;
import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class AsukaItem {

    private NamespacedKey id;
    private ItemStack itemStack;

    public AsukaItem(NamespacedKey id, ItemStack itemStack) {
        this.id = id;
        this.itemStack = itemStack;
        this.resetItemInfo();
    }

    public NamespacedKey getId() {
        return this.id;
    }

    public ItemStack getItemStack () {
        return this.itemStack;
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
    }

    public static JsonObject serialize(AsukaItem asukaItem) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", asukaItem.id.toString());

        jsonObj.addProperty("item_stack", AsukaMeow
                .INSTANCE
                .getNMSManager()
                .getItemFactory()
                .serialize(asukaItem.itemStack));

        return jsonObj;
    }

    public static AsukaItem deserialize(JsonObject jsonObj) {
        String[] keys = jsonObj.get("id").getAsString().split(":");
        NamespacedKey key = new NamespacedKey(keys[0], keys[1]);

        ItemStack itemStack = AsukaMeow
                .INSTANCE
                .getNMSManager()
                .getItemFactory()
                .deserialize(jsonObj.get("item_stack").getAsString());

        return new AsukaItem(key, itemStack);
    }

}
