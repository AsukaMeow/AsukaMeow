package at.meowww.AsukaMeow.nms.v1_14_R1;

import at.meowww.AsukaMeow.user.UserInventory;
import net.minecraft.server.v1_14_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_14_R1.NBTList;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

public class PlayerFactory extends at.meowww.AsukaMeow.nms.PlayerFactory {

    @Override
    public UserInventory getOfflinePlayerInventory(UUID uuid) {
        String worldFolderPath = Bukkit.getServer().getWorldContainer().getAbsolutePath();
        File playerDataFile = new File(worldFolderPath, "/world/playerdata/" + uuid + ".dat");
        ItemStack[] armorStacks = new ItemStack[4];
        ItemStack[] storeStacks = new ItemStack[36];
        ItemStack[] extraStacks = new ItemStack[1];
        try {
            if (playerDataFile.exists()) {
                NBTTagCompound playerNBT = NBTCompressedStreamTools.a(new FileInputStream(playerDataFile));
                ((NBTList<NBTTagCompound>) playerNBT.get("Inventory")).forEach((each) -> {
                    int slotIndex = Byte.toUnsignedInt(each.getByte("Slot"));
                    ItemStack itemStack = CraftItemStack.asBukkitCopy(
                            net.minecraft.server.v1_14_R1.ItemStack.a(each));
                    each.remove("Slot");
                    if (slotIndex < 36) {
                        storeStacks[slotIndex] = itemStack;
                    } else if (slotIndex == 150) {
                        extraStacks[0] = itemStack;
                    } else {
                        armorStacks[slotIndex - 100] = itemStack;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserInventory(armorStacks, storeStacks, extraStacks);
    }
}
