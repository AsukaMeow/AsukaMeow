package at.meowww.AsukaMeow.item;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.item.feature.FeatureBinding;
import at.meowww.AsukaMeow.item.feature.FeatureTime;
import at.meowww.AsukaMeow.item.feature.IFeature;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
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

    /**
     * Some Feature may modified Player's hand. So getItem method in event is not proper.
     * By examine the event hand is main or off, to give item in main hand or off hand.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL))
            return;
        
        if(event.getHand().equals(EquipmentSlot.HAND))
            sendSingleToFeature(event.getPlayer().getInventory().getItemInMainHand(), event);
        else if(event.getHand().equals(EquipmentSlot.OFF_HAND))
            sendSingleToFeature(event.getPlayer().getInventory().getItemInOffHand(), event);
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
                if (feat != null && ((FeatureBinding) feat).getType() == FeatureBinding.Type.UNDROPABLE)
                    keep.add(stack);
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;

        Player player = (Player) event.getPlayer();
        ItemStack[] stacks = player.getInventory().getContents();
        for (int i = 0; i< stacks.length; ++i) {
            if (AsukaMeow.INSTANCE.getNMSManager().getFeatureFactory().hasFeature(stacks[i])) {
                IFeature feat = AsukaMeow.INSTANCE
                        .getNMSManager()
                        .getFeatureFactory()
                        .manualDeserialize(FeatureTime.name, stacks[i]);
                if (feat != null)
                    ((FeatureTime) feat).examineReplace(player, i);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemHeld(PlayerItemHeldEvent event) {
        sendSingleToFeature(event.getPlayer()
                .getInventory().getItem(event.getNewSlot()), event);
    }

}
