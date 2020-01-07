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
    public <T extends Event> void trigger(ItemStack itemStack, T event) {
        if (event instanceof PlayerDropItemEvent)
            onDrop(itemStack, (PlayerDropItemEvent) event);
        else if (event instanceof InventoryClickEvent)
            onInventoryClickSlot(itemStack, (InventoryClickEvent) event);
    }

    @Override
    public <T extends Event> void updateLore(ItemStack item, T event) {
        updateLore(item);
    }

    public void updateLore(ItemStack item) {
        List<String> lores = item.getLore();
        if (!lores.contains("§3靈魂綁定")) {
            if (type == Type.UNDROPABLE)
                lores.add(0, "§3靈魂綁定");
            item.setLore(lores);
        }
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

    public abstract void onDrop(ItemStack item, PlayerDropItemEvent event);

    public abstract void onInventoryClickSlot(ItemStack item, InventoryClickEvent event);

    public enum Type {
        DROPABLE,
        UNDROPABLE;
    }

}
