package at.meowww.AsukaMeow.item.feature;

import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public abstract class FeatureBinding implements IFeature {

    public static final String name = "BINDING";
    public static final String lowerName = name.toLowerCase();
    protected Type type;

    public FeatureBinding () {}

    public FeatureBinding(Type type) {
        this.type = type;
    }

    public Type getType () {
        return this.type;
    }

    @Override
    public <T extends Event> ItemStack trigger(ItemStack itemStack, T event) {
        if (event instanceof PlayerDropItemEvent)
            itemStack = onDrop(itemStack, (PlayerDropItemEvent) event);
        else if (event instanceof InventoryClickEvent)
            itemStack = onInventoryClickSlot(itemStack, (InventoryClickEvent) event);

        return itemStack;
    }

    @Override
    public <T extends Event> ItemStack updateLore(ItemStack item, T event) {
        return updateLore(item);
    }

    public ItemStack updateLore(ItemStack itemStack) {
        List<String> lores = itemStack.getLore();
        if (type == Type.UNDROPABLE && !lores.contains("§3靈魂綁定"))
                lores.add(0, "§3靈魂綁定");
        else if (type == Type.DROPABLE && lores.contains("§3靈魂綁定"))
                lores.remove("§3靈魂綁定");
        itemStack.setLore(lores);
        return itemStack;
    }

    public int hashCode() {
        return Objects.hashCode(type);
    }

    public static JsonObject serialize(IFeature feat) {
        FeatureBinding feature = (FeatureBinding) feat;
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", FeatureBinding.name);
        jsonObj.addProperty("type", feature.type.name());
        return jsonObj;
    }

    public abstract ItemStack onDrop(ItemStack item, PlayerDropItemEvent event);

    public abstract ItemStack onInventoryClickSlot(ItemStack item, InventoryClickEvent event);

    public enum Type {
        DROPABLE,
        UNDROPABLE;
    }

}
