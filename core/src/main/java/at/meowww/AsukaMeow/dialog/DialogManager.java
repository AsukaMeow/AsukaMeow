package at.meowww.AsukaMeow.dialog;

import at.meowww.AsukaMeow.AsukaMeow;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.util.Consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DialogManager {

    private static final Map<UUID, Consumer<Player>> callbacks = new HashMap<>();

    public void registerListener () {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void callbackCommand(PlayerCommandPreprocessEvent event) {
                if (event.getMessage().startsWith("/asukameow:callback")) {
                    String[] args = event.getMessage().split(" ");
                    if (args.length == 2 && args[1].split("-").length == 5) {
                        UUID uuid = UUID.fromString(args[1]);
                        Consumer<Player> callback = callbacks.remove(uuid);
                        if (callback != null)
                            callback.accept(event.getPlayer());
                        event.setCancelled(true);
                    }
                }
            }
        }, AsukaMeow.INSTANCE);
    }

    public static ClickEvent registerCallback (Consumer<Player> consumer) {
        UUID uuid = UUID.randomUUID();
        callbacks.put(uuid, consumer);
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/asukameow:callback " + uuid);
    }

    public static ComponentBuilder registerCallback (ComponentBuilder builder, Consumer<Player> consumer) {
        UUID uuid = UUID.randomUUID();
        callbacks.put(uuid, consumer);
        return builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/asukameow:callback " + uuid));
    }

}
