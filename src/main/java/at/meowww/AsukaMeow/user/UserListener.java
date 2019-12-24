package at.meowww.AsukaMeow.user;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

    private UserManager manager;

    public UserListener (UserManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        User onlineUser = manager.findUser(uuid);
        if (onlineUser == null) {
            onlineUser = User.newUser(player);
        }
        onlineUser.online(player);
        manager.onlineUser.put(uuid, onlineUser);
        manager.updateUser(onlineUser);

        player.setDisplayName(onlineUser.getDisplayName());
        player.setPlayerListName(onlineUser.getDisplayName());
    }

    @EventHandler
    public void onPlayerLeave (PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        User offlineUser = manager.onlineUser.remove(uuid);
        offlineUser.offline(event.getPlayer().getLocation());
        manager.updateUser(offlineUser);
    }
}
