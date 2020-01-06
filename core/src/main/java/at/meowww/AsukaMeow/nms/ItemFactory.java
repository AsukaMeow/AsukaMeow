package at.meowww.AsukaMeow.nms;

import at.meowww.AsukaMeow.item.AsukaItem;
import org.bukkit.inventory.ItemStack;

/**
 * For any ItemStack interact with NMS
 *
 * This abstract class should be extends by relative version
 *  of NMS class.
 * @author clooooode
 * @since 0.0.3-SNAPSHOT
 */
public abstract class ItemFactory {

    public abstract void updateOnlinePlayerInventory();

    /**
     * Serialize a ItemStack to Minecraft format string
     * @param itemStack
     * @return string with Minecraft's format
     */
    public abstract String serialize (ItemStack itemStack);

    /**
     * Deserialize a Minecraft format string to ItemStack
     * @param str
     * @return
     */
    public abstract ItemStack deserialize (String str);

    /**
     * Convert a AsukaItem to ItemStack with proper NBT Tag set.
     *
     * This method should take an exists AsukaItem build a ItemStack with
     *  feature NBT Tag value write inside.
     * @param asukaItem
     * @return ItemStack with feature's NBT Tags
     */
    public abstract ItemStack toItemStack(AsukaItem asukaItem);

    /**
     * Check the given ItemStack's NBT Tag to determine an update from origin AsukaItem
     *  or clear its NBTTag or do nothing.
     * @param itemStack The ItemStack to be examine.
     * @return The ItemStack after examine.
     */
    public abstract ItemStack itemStackUpdate(ItemStack itemStack);

}
