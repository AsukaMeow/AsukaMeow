package at.meowww.AsukaMeow.item.feature;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public interface IFeature {

    <T extends Event> void trigger(ItemStack itemStack, T event);

    <T extends Event> void updateLore (ItemStack item, T event);

    void updateLore (ItemStack item);

    ItemStack serialize(ItemStack itemStack);

}
