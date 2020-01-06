package at.meowww.AsukaMeow.item.feature;

import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
    public abstract <T extends Event> void trigger(ItemStack itemStack, T event);

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

    public void onDrop(ItemStack item, PlayerDropItemEvent event) {
        if (type == Type.UNDROPABLE) {
            event.getPlayer().sendActionBar("靈魂綁定的物品不能被丟棄");
            event.setCancelled(true);
        }
    }

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

    public int hashCode() {
        return Objects.hashCode(type);
    }

    public abstract ItemStack serialize(ItemStack itemStack);

    public static JsonObject serialize(IFeature feat) {
        FeatureBinding feature = (FeatureBinding) feat;
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", FeatureBinding.name);
        jsonObj.addProperty("type", feature.type.name());
        return jsonObj;
    }

    public abstract FeatureBinding deserialize(ItemStack itemStack);

    public enum Type {
        DROPABLE,
        UNDROPABLE;
    }
}
