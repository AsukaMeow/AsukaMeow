package at.meowww.AsukaMeow.nms.v1_14_R1;

import at.meowww.AsukaMeow.item.AsukaItem;
import at.meowww.AsukaMeow.item.feature.Feature;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_14_R1.MojangsonParser;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemFactory extends at.meowww.AsukaMeow.nms.ItemFactory {

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

    public AsukaItem getAsukaItem(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound asukaTag = nmsStack.getTag().getCompound("AsukaItem");
        String[] keys = asukaTag.getString("name").split(":");
        NamespacedKey name = new NamespacedKey(keys[0], keys[1]);
        Set<Feature> featureArr = AsukaItem.featuresDeserialize(new JsonParser().parse(
                asukaTag.getString("feature")).getAsJsonArray());
        return new AsukaItem(name, itemStack, featureArr);
    }

    public ItemStack setAsukaItem(AsukaItem asukaItem) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(asukaItem.getItemStack());
        NBTTagCompound asukaTag = new NBTTagCompound();
        asukaTag.setString("name", asukaItem.getId().toString());
        asukaTag.setString("feature", AsukaItem
                .featuresSerialize(asukaItem).toString());

        NBTTagCompound tagCompound = nmsStack.getOrCreateTag();
        tagCompound.set("AsukaItem", asukaTag);
        nmsStack.setTag(tagCompound);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }
    
}
