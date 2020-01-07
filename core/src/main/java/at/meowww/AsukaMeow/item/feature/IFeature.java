package at.meowww.AsukaMeow.item.feature;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public interface IFeature {

    <T extends Event> ItemStack trigger(ItemStack itemStack, T event);

    <T extends Event> ItemStack updateLore (ItemStack item, T event);

    ItemStack updateLore (ItemStack itemStack);

    ItemStack update(ItemStack itemStack);

    /**
     * This method serialize the data in feature into proper NBT format
     *  and add into ItemStack's NBT tags.
     * @param itemStack stack without feature's NBT data
     * @return stack with feature's NBT data write in tag.
     */
    ItemStack serialize(ItemStack itemStack);

    IFeature deserialize(ItemStack itemStack);

    int hashCode();
}
