package at.meowww.AsukaMeow.nms.v1_14_R1;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.item.AsukaItem;
import at.meowww.AsukaMeow.item.feature.IFeature;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_14_R1.MojangsonParser;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemFactory extends at.meowww.AsukaMeow.nms.ItemFactory {

    public void updateOnlinePlayerInventory() {
        ItemStack[] contents;
        for(Player player : Bukkit.getOnlinePlayers()) {
            contents = player.getInventory().getContents();
            for (int i = 0; i < contents.length; i++)
                player.getInventory().setItem(
                        i, itemStackUpdate(contents[i]));
            if (player.getOpenInventory() != null) {
                ItemStack cursorStack = player.getOpenInventory().getCursor();
                if (cursorStack != null)
                    player.getOpenInventory().setCursor(itemStackUpdate(cursorStack));
            }
            player.updateInventory();
        }
    }

    public String serialize (ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack stack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbt = new NBTTagCompound();
        stack.save(nbt);
        return nbt.toString();
    }

    public ItemStack deserialize(String str) {
        try {
            NBTTagCompound nbt = MojangsonParser.parse(str);
            net.minecraft.server.v1_14_R1.ItemStack stack =
                    net.minecraft.server.v1_14_R1.ItemStack.a(nbt);
            return CraftItemStack.asBukkitCopy(stack);
        } catch (CommandSyntaxException cse) {
            return null;
        }
    }

    public ItemStack toItemStack(AsukaItem asukaItem) {
        ItemStack itemStack = asukaItem.getItemStack();
        for(IFeature feature : asukaItem.getFeatures())
            itemStack = AsukaMeow.INSTANCE.getNMSManager()
                    .getFeatureFactory()
                    .serialize(feature, itemStack);

        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tagCom = nmsStack.getTag();
        tagCom.setString("base_item", asukaItem.getId().toString());
        tagCom.setInt("base_hash", asukaItem.hash());
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public ItemStack itemStackUpdate(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        // May contain features.
        if (nmsStack.hasTag()) {
            NBTTagCompound tagCom = nmsStack.getTag();
            // Clone from AsukaItem
            if (tagCom.hasKey("base_item")) {
                AsukaItem asukaItem = AsukaMeow.INSTANCE
                        .getItemManager()
                        .getAsukaItem(tagCom.getString("base_item"));
                if (asukaItem == null) {
                    // The AsukaItem is no longer exists, clear its feature.
                    tagCom.remove("base_item");
                    tagCom.remove("base_hash");
                    tagCom.remove("feature");
                    return CraftItemStack.asBukkitCopy(nmsStack);
                } else if (asukaItem.hash() != tagCom.getInt("base_hash")) {
                    // Such AsukaItem is changed in current version, update it.
                    for(IFeature feature : asukaItem.getFeatures())
                        itemStack = AsukaMeow.INSTANCE.getNMSManager()
                                .getFeatureFactory()
                                .featureUpdate(feature, itemStack);

                    net.minecraft.server.v1_14_R1.ItemStack updateNmsStack =
                            CraftItemStack.asNMSCopy(itemStack);
                    NBTTagCompound updateTagCom = updateNmsStack.getTag();
                    updateTagCom.setInt("base_hash", asukaItem.hash());
                    return CraftItemStack.asBukkitCopy(updateNmsStack);
                }
            }
        }
        return itemStack;
    }

}
