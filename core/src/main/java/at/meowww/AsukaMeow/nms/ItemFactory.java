package at.meowww.AsukaMeow.nms;

import org.bukkit.inventory.ItemStack;

public abstract class ItemFactory {

    public abstract String serialize (ItemStack itemStack);

    public abstract ItemStack deserialize (String str);

}
