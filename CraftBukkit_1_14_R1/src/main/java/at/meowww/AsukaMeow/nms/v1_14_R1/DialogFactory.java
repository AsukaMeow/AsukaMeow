package at.meowww.AsukaMeow.nms.v1_14_R1;

import at.meowww.AsukaMeow.dialog.Dialog;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenBook;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DialogFactory extends at.meowww.AsukaMeow.nms.DialogFactory {

    @Override
    public ItemMeta setPageOverLimit (ItemMeta meta, BaseComponent ... pages) {
        CraftMetaBook bookMeta = (CraftMetaBook) meta;
        bookMeta.pages.clear();
        for (BaseComponent c : pages) {
            bookMeta.pages.add(IChatBaseComponent.ChatSerializer.a(
                    ComponentSerializer.toString(c))
            );
        }
        return meta;
    }

    @Override
    public ItemStack toBook (Dialog dialog) {
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);
        ItemMeta meta = stack.getItemMeta();

        setPageOverLimit(meta, dialog.toComponents());
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public void openDialog (Player player, Dialog dialog) {
        int slotIndex = player.getInventory().getHeldItemSlot();
        ItemStack oldSlot = player.getInventory().getItem(slotIndex);

        player.getInventory().setItem(slotIndex, toBook(dialog));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new PacketPlayOutOpenBook(EnumHand.MAIN_HAND)
        );
        player.getInventory().setItem(slotIndex, oldSlot);
    }

}
