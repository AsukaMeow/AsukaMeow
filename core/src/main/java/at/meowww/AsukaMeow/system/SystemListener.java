package at.meowww.AsukaMeow.system;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class SystemListener implements Listener {

    private SystemManager manager;

    public SystemListener(SystemManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(ServerCommandEvent event) {
        if (event.getCommand().equalsIgnoreCase("stop")) {
            event.setCancelled(true);
            manager.getEntrance().entranceShutdown();
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.kickPlayer("伺服器例行性維護停機中")
            );
            Bukkit.getScheduler().runTaskTimer(AsukaMeow.INSTANCE, () -> {
                if (AsukaMeow.INSTANCE.getUserManager().getOnlineUser().isEmpty())
                    Bukkit.shutdown();
            }, 20, 20);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnimalBred(EntityBreedEvent event) {
        event.setCancelled(manager.isCanBreed());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchant(EnchantItemEvent event) {
        Bukkit.getScheduler().runTaskLater(AsukaMeow.INSTANCE, () -> {
            if (!manager.isCanEnchant() || !Utils.isChanceTriggered(manager.getEnchantSuccessChance())) {
                event.getEnchantsToAdd().forEach(((enchantment, integer) ->
                        event.getItem().removeEnchantment(enchantment)));
                if (manager.isAnnounceEnchantFail()) {
                    String failMessage = "玩家["
                            + event.getEnchanter().getDisplayName() + "]在嘗試附魔道具["
                            + (event.getItem().getItemMeta().hasDisplayName() ?
                            event.getItem().getItemMeta().getDisplayName() :
                            event.getItem().getI18NDisplayName())
                            +"]的時候失敗了..";
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(failMessage));
                }
            }
        }, 1L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFish(PlayerFishEvent event) {
        Entity caught = event.getCaught();
        if (caught != null && caught instanceof Item) {
            Item caughtItem = (Item) caught;
            if (caughtItem.getItemStack().getData().equals(Material.ENCHANTED_BOOK)) {
                event.setCancelled(!manager.isCanEnchant());
                AsukaMeow.INSTANCE.getLogger().info(event.getPlayer().getDisplayName()
                        + " fished an enchanted book, and "
                        + (event.isCancelled() ? " canceled" : "not canceled")
                );
            }
        }
    }

}
