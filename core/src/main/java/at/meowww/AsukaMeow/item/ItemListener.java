package at.meowww.AsukaMeow.item;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.item.feature.FeatureBinding;
import at.meowww.AsukaMeow.item.feature.IFeature;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemListener implements Listener {

    private ItemManager manager;

    private Map<UUID, List<ItemStack>> keepInvList = new HashMap<>();

    public ItemListener (ItemManager manager) {
        this.manager = manager;
    }

    private void sendSingleToFeature(ItemStack itemStack, Event event) {
        if (AsukaMeow.INSTANCE.getNMSManager().getFeatureFactory().hasFeature(itemStack)) {
            AsukaMeow.INSTANCE.getNMSManager()
                    .getFeatureFactory().trigger(itemStack, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemInteract(PlayerInteractEvent event) {
        sendSingleToFeature(event.getItem(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event) {
        sendSingleToFeature(event.getItemDrop().getItemStack(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(PlayerRespawnEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (keepInvList.containsKey(uuid)) {
            List<ItemStack> items = keepInvList.remove(uuid);
            event.getPlayer().getInventory().addItem(
                    items.toArray(new ItemStack[items.size()])
            );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        List<ItemStack> keep = new ArrayList<>();
        for (ItemStack stack : event.getDrops()) {
            // Find AsukaItem to check feature.
            if (AsukaMeow.INSTANCE.getNMSManager().getFeatureFactory().hasFeature(stack)) {
                IFeature feat = AsukaMeow.INSTANCE
                        .getNMSManager()
                        .getFeatureFactory()
                        .manualDeserialize(FeatureBinding.name, stack);
                if (feat != null) {
                    if (((FeatureBinding) feat).getType() == FeatureBinding.Type.UNDROPABLE)
                        keep.add(stack);
                }
            }
        }
        event.getDrops().removeAll(keep);
        keepInvList.put(event.getEntity().getPlayer().getUniqueId(), keep);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        /* InventoryClickEvent sometimes would not fire properly under Creative mode.
         * So the ItemStack update statement will not function.
         */
        // Examine clicked slot's ItemStack.
        event.setCurrentItem(AsukaMeow.INSTANCE
                .getNMSManager()
                .getItemFactory()
                .itemStackUpdate(event.getCurrentItem()));

        if (event.isShiftClick()) {
            sendSingleToFeature(event.getCurrentItem(), event);
        } else {
            sendSingleToFeature(event.getCursor(), event);
        }
    }

}
