package at.meowww.AsukaMeow.nms;

import at.meowww.AsukaMeow.dialog.Dialog;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class DialogFactory {

    public abstract ItemMeta addPageOverLimit(ItemMeta meta, String ... pages);

    public abstract ItemMeta setPageOverLimit(ItemMeta meta, List<String> pages);

    public abstract ItemMeta setPageOverLimit(ItemMeta meta, String ... pages);

    public abstract ItemStack toBook (Dialog dialog);

    public abstract void openDialog (Player player, Dialog dialog);

}
