package at.meowww.AsukaMeow.territory;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.user.User;
import com.destroystokyo.paper.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TerritoryListener implements Listener {

    private TerritoryManager manager;

    public TerritoryListener (TerritoryManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo().equals(event.getFrom()))
            return;

        Player player = event.getPlayer();
        User user = AsukaMeow.INSTANCE.getUserManager().getUser(player.getUniqueId());
        Territory territory = manager.enterTerritory(event.getTo());
        if (territory == null) {
            user.setCurrentTerritoryId("");
        } else if (!user.getCurrentTerritoryId().equalsIgnoreCase(territory.getId())) {
            user.setCurrentTerritoryId(territory.getId());
            player.sendTitle(
                    new Title(territory.getTitle(), territory.getSubtitle())
            );
        }
    }

}
