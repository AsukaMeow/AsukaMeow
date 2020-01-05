package at.meowww.AsukaMeow.item;

import at.meowww.AsukaMeow.AsukaMeow;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {

    private ItemManager manager;

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

}
