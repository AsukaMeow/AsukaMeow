package at.meowww.AsukaMeow.item;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.item.feature.Feature;
import at.meowww.AsukaMeow.item.feature.FeatureBinding;
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
        if (AsukaMeow.INSTANCE
                .getNMSManager()
                .getItemFactory()
                .isAsukaItem(itemStack)
        ) {
            AsukaItem asukaItem = AsukaMeow.INSTANCE
                    .getNMSManager()
                    .getItemFactory()
                    .getAsukaItem(itemStack);
            asukaItem.getFeatures()
                    .forEach((feature) -> feature.trigger(asukaItem, event));

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemInteract(PlayerInteractEvent event) {
        ItemStack useStack = event.getItem();
        sendSingleToFeature(useStack, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack dropStack = event.getItemDrop().getItemStack();
        sendSingleToFeature(dropStack, event);
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
            if (stack != null && AsukaMeow
                    .INSTANCE
                    .getNMSManager()
                    .getItemFactory()
                    .isAsukaItem(stack)
            ) {
                AsukaItem asukaItem = AsukaMeow.INSTANCE
                        .getNMSManager()
                        .getItemFactory()
                        .getAsukaItem(stack);
                for (Feature feat : asukaItem.getFeatures()) {
                    // Find Binding Feature
                    if (feat instanceof FeatureBinding) {
                        // Found & check can drop or not.
                        if (((FeatureBinding) feat).getType() == FeatureBinding.Type.UNDROPABLE)
                            // Don't  drop.
                            keep.add(stack);
                        // Whether can drop or not. Just break to next item;
                        break;
                    }
                }
            }
        }
        event.getDrops().removeAll(keep);
        keepInvList.put(event.getEntity().getPlayer().getUniqueId(), keep);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isShiftClick()) {
            sendSingleToFeature(event.getCurrentItem(), event);
        } else {
            sendSingleToFeature(event.getCursor(), event);
        }
    }

}
