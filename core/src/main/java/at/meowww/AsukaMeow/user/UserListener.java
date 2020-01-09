package at.meowww.AsukaMeow.user;

import at.meowww.AsukaMeow.AsukaMeow;
import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

    private UserManager manager;

    public UserListener (UserManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin (PlayerLoginEvent event) {
        if (!AsukaMeow.INSTANCE.getSystemManager().getEntrance().isAllow())
            if (!event.getPlayer().isOp())
                event.disallow(
                        PlayerLoginEvent.Result.KICK_OTHER,
                        AsukaMeow.INSTANCE.getSystemManager().getEntrance().getDisallowMsg());
            else
                Bukkit.getScheduler().runTaskLater(AsukaMeow.INSTANCE, () ->
                    event.getPlayer().sendTitle(
                            new Title("當前登入入口狀態：關閉", "維護完成請記得開啟入口"))
                , 20 * 5L);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        User onlineUser = manager.findUser(uuid);
        if (onlineUser == null) {
            onlineUser = User.newUser(player);
            manager.updateUser(onlineUser);
        }
        onlineUser.online(player);
        manager.onlineUser.put(uuid, onlineUser);

        player.setDisplayName(onlineUser.getDisplayName());
        player.setPlayerListName(onlineUser.getDisplayName());

        AsukaMeow.INSTANCE
                .getSystemManager()
                .getAnnouncement()
                .sendAnnouncement(onlineUser);
    }

    @EventHandler
    public void onPlayerLeave (PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        User offlineUser = manager.onlineUser.remove(uuid);
        offlineUser.offline(event.getPlayer());
        manager.updateUser(offlineUser);
        AsukaMeow.INSTANCE.getLogger().info("Player ["
                + offlineUser.getDisplayName()
                + "] data saved."
        );
    }

}
