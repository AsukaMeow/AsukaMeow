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

    public abstract AsukaItem getAsukaItem(ItemStack itemStack);

    public abstract ItemStack setAsukaItem(AsukaItem asukaItem);

}
