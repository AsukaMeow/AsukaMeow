package at.meowww.AsukaMeow.nms.v1_14_R1;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.item.AsukaItem;
import at.meowww.AsukaMeow.item.feature.IFeature;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_14_R1.MojangsonParser;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

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

    public ItemStack toItemStack(AsukaItem asukaItem) {
        ItemStack itemStack = asukaItem.getItemStack();
        for(IFeature feature : asukaItem.getFeatures())
            itemStack = AsukaMeow.INSTANCE.getNMSManager()
                    .getFeatureFactory()
                    .serialize(feature, itemStack);
        return itemStack;
    }

    public boolean hasFeature(ItemStack itemStack) {
        net.minecraft.server.v1_14_R1.ItemStack nmsStack =
                CraftItemStack.asNMSCopy(itemStack);
        if (!nmsStack.hasTag())
            return false;
        return nmsStack.getTag().hasKey("feature");
    }

}
