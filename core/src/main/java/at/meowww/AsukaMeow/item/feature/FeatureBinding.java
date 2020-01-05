package at.meowww.AsukaMeow.item.feature;

import at.meowww.AsukaMeow.item.AsukaItem;
import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class FeatureBinding extends Feature {

    public static final String name = "BINDING";
    private Type type;

    public FeatureBinding(Type type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public Type getType () {
        return this.type;
    }

    @Override
    public <T extends Event> void trigger(AsukaItem item, T event) {
        if (event instanceof  PlayerDropItemEvent)
            onDrop(item, (PlayerDropItemEvent) event);
        else if (event instanceof  InventoryClickEvent)
            onInventoryClickSlot(item, (InventoryClickEvent) event);
    }

    @Override
    public <T extends Event> void updateLore(AsukaItem item, T event) {
        updateLore(item);
    }

    @Override
    public void updateLore(AsukaItem item) {
        List<String> lores = item.getItemStack().getLore();
        if (!lores.contains("§3靈魂綁定")) {
            if (type == Type.UNDROPABLE)
                lores.add(0, "§3靈魂綁定");
            item.getItemStack().setLore(lores);
        }
    }

    public void onDrop(AsukaItem item, PlayerDropItemEvent event) {
        if (type == Type.UNDROPABLE) {
            event.getPlayer().sendActionBar("靈魂綁定的物品不能被丟棄");
            event.setCancelled(true);
        }
    }

    public void onInventoryClickSlot(AsukaItem item, InventoryClickEvent event) {
        if (type == Type.UNDROPABLE) {
            System.out.println(event.getInventory().getType());
            if (event.isShiftClick())
                event.setCancelled(!event.getInventory().getType().equals(InventoryType.CRAFTING));
            else if (!(event.getClickedInventory() instanceof PlayerInventory))
                event.setCancelled(true);
        }
    }

    public static JsonObject serialize(Feature feat) {
        FeatureBinding feature = (FeatureBinding) feat;
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("name", feature.getName());
        jsonObj.addProperty("type", feature.type.name());
        return jsonObj;
    }

    public static FeatureBinding deserialize(JsonObject jsonObj) {
        return new FeatureBinding(
                Type.valueOf(jsonObj.get("type").getAsString().toUpperCase())
        );
    }

    public enum Type {
        DROPABLE,
        UNDROPABLE;
    }
}
