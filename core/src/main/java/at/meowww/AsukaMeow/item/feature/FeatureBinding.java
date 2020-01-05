package at.meowww.AsukaMeow.item.feature;

import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public abstract class FeatureBinding implements IFeature {

    public static final String name = "BINDING";
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
            if (event.isShiftClick())
                event.setCancelled(!event.getInventory().getType().equals(InventoryType.CRAFTING));
            else if (!(event.getClickedInventory() instanceof PlayerInventory))
                event.setCancelled(true);
        }
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
