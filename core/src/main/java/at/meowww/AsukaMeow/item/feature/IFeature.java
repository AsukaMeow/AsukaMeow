package at.meowww.AsukaMeow.item.feature;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public interface IFeature {

    <T extends Event> void trigger(ItemStack itemStack, T event);

    <T extends Event> void updateLore (ItemStack item, T event);

    void updateLore (ItemStack item);

    /**
     * This method serialize the data in feature into proper NBT format
     *  and add into ItemStack's NBT tags.
     * @param itemStack stack without feature's NBT data
     * @return stack with feature's NBT data write in tag.
     */
    ItemStack serialize(ItemStack itemStack);

    int hashCode();
}
