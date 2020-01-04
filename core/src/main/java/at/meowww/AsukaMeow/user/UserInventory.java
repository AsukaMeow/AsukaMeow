package at.meowww.AsukaMeow.user;

import at.meowww.AsukaMeow.AsukaMeow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class UserInventory {

    private ItemStack[] armorStacks;
    private ItemStack[] storeStacks;
    private ItemStack[] extraStacks;

    public UserInventory (ItemStack[] armorStacks, ItemStack[] storeStacks, ItemStack[] extraStacks) {
        this.armorStacks = armorStacks;
        this.storeStacks = storeStacks;
        this.extraStacks = extraStacks;
    }

    public UserInventory (PlayerInventory playerInv) {
        this(playerInv.getArmorContents(), playerInv.getStorageContents(), playerInv.getExtraContents());
    }

    public boolean allEmpty () {
        for(ItemStack stack : armorStacks)
            if (stack != null)
                return false;
        for(ItemStack stack : storeStacks)
            if (stack != null)
                return false;
        for(ItemStack stack : extraStacks)
            if (stack != null)
                return false;
        return true;
    }

    public void presetInventory (Player player) {
        player.getInventory().setArmorContents(armorStacks);
        player.getInventory().setStorageContents(storeStacks);
        player.getInventory().setExtraContents(extraStacks);
    }

    public void postInventory (Player player) {
        armorStacks = player.getInventory().getArmorContents();
        storeStacks = player.getInventory().getStorageContents();
        extraStacks = player.getInventory().getExtraContents();
        player.getInventory().clear();
    }

    // INV_NAME, INV_DATA
    public Map<String, Object> serialize () {
        Map<String, Object> userInvMap = new HashMap<>();

        // SLOT_INDEX, ITEM_STACK
        Map<String, Object> armorMap = new HashMap<>();
        armorMap.put("size", armorStacks.length);
        for (int i = 0; i < armorStacks.length; ++i) {
            if (armorStacks[i] != null) {
                armorMap.put(
                        String.valueOf(i),
                        AsukaMeow.INSTANCE
                                .getNMSManager()
                                .getItemFactory()
                                .serialize(armorStacks[i])
                );
            }
        }
        userInvMap.put("armor", armorMap);

        // SLOT_INDEX, ITEM_STACK
        Map<String, Object> invMap = new HashMap<>();
        invMap.put("size", storeStacks.length);
        for (int i = 0; i < storeStacks.length; ++i) {
            if (storeStacks[i] != null) {
                invMap.put(
                        String.valueOf(i),
                        AsukaMeow.INSTANCE
                                .getNMSManager()
                                .getItemFactory()
                                .serialize(storeStacks[i])
                );
            }
        }
        userInvMap.put("main_inventory", invMap);

        // SLOT_INDEX, ITEM_STACK
        Map<String, Object> extMap = new HashMap<>();
        extMap.put("size", extraStacks.length);
        for (int i = 0; i < extraStacks.length; ++i) {
            if (extraStacks[i] != null) {
                extMap.put(
                        String.valueOf(i),
                        AsukaMeow.INSTANCE
                                .getNMSManager()
                                .getItemFactory()
                                .serialize(extraStacks[i])
                );
            }
        }
        userInvMap.put("extra", extMap);


        return userInvMap;
    }

    public static UserInventory deserialize (Map<String, Object> map) {
        Map<String, Object> armorMap = (Map<String, Object>) map.get("armor");
        ItemStack[] armorStacks;
        if (armorMap != null) {
            int armorSize = Integer.valueOf(armorMap.get("size").toString());
            armorStacks = new ItemStack[armorSize];
            for (int i = 0; i < armorSize; ++i) {
                String index = String.valueOf(i);
                if (armorMap.containsKey(index)) {
                    armorStacks[i] = AsukaMeow.INSTANCE
                            .getNMSManager()
                            .getItemFactory()
                            .deserialize((String) armorMap.get(index));
                }
            }
        } else {
            armorStacks = new ItemStack[4];
        }

        Map<String, Object> invMap = (Map<String, Object>) map.get("main_inventory");
        ItemStack[] storeStacks;
        if (invMap != null) {
            int invSize = Integer.valueOf(invMap.get("size").toString());
            storeStacks = new ItemStack[invSize];
            for (int i = 0; i < invSize; ++i) {
                String index = String.valueOf(i);
                if (invMap.containsKey(index)) {
                    storeStacks[i] = AsukaMeow.INSTANCE
                            .getNMSManager()
                            .getItemFactory()
                            .deserialize((String) invMap.get(index));
                }
            }
        } else {
            storeStacks = new ItemStack[36];
        }

        Map<String, Object> extMap = (Map<String, Object>) map.get("extra");
        ItemStack[] extraStacks;
        if (extMap != null) {
            int extraSize = Integer.valueOf(extMap.get("size").toString());
            extraStacks = new ItemStack[extraSize];
            for (int i = 0; i < extraSize; ++i) {
                String index = String.valueOf(i);
                if (extMap.containsKey(index)) {
                    extraStacks[i] = AsukaMeow.INSTANCE
                            .getNMSManager()
                            .getItemFactory()
                            .deserialize((String) extMap.get(index));
                }
            }
        } else {
            extraStacks = new ItemStack[1];
        }

        return new UserInventory(armorStacks, storeStacks, extraStacks);
    }
}
