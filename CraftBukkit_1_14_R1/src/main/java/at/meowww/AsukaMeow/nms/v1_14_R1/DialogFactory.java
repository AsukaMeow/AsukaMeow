package at.meowww.AsukaMeow.nms.v1_14_R1;

import at.meowww.AsukaMeow.dialog.Dialog;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenBook;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.List;

public class DialogFactory extends at.meowww.AsukaMeow.nms.DialogFactory {

    @Override
    public ItemMeta addPageOverLimit(ItemMeta meta, String ... pages) {
        CraftMetaBook bookMeta = (CraftMetaBook) meta;
        String[] var5 = pages;
        int var4 = pages.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            String page = var5[var3];
            if (page == null)
                page = "";
            bookMeta.pages.add(CraftChatMessage.fromString(page, true)[0]);
        }
        return bookMeta;
    }

    @Override
    public ItemMeta setPageOverLimit (ItemMeta meta, List<String> pages) {
        CraftMetaBook bookMeta = (CraftMetaBook) meta;
        bookMeta.pages.clear();
        Iterator var3 = pages.iterator();

        while(var3.hasNext()) {
            String page = (String)var3.next();
            addPageOverLimit(meta, page);
        }
        return meta;
    }

    @Override
    public ItemMeta setPageOverLimit (ItemMeta meta, String... pages) {
        CraftMetaBook bookMeta = (CraftMetaBook) meta;
        String[] var5 = pages;
        int var4 = pages.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            String page = var5[var3];
            if (page == null) {
                page = "";
            }
            bookMeta.pages.add(CraftChatMessage.fromString(page, true)[0]);
        }
        return bookMeta;
    }

    @Override
    public ItemStack toBook (Dialog dialog) {
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);
        ItemMeta meta = stack.getItemMeta();

        setPageOverLimit(meta, dialog.toList());
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
