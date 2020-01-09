package at.meowww.AsukaMeow.system;

import at.meowww.AsukaMeow.AsukaMeow;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class SystemListener implements Listener {

    private SystemManager manager;

    public SystemListener(SystemManager manager) {
        this.manager = manager;
    }

    @EventHandler
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

}
