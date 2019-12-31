package at.meowww.AsukaMeow.nms;

import at.meowww.AsukaMeow.dialog.Dialog;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class DialogFactory {

    public abstract ItemMeta setPageOverLimit (ItemMeta meta, BaseComponent ... pages);

    public abstract ItemStack toBook (Dialog dialog);

    public abstract void openDialog (Player player, Dialog dialog);

}
