package at.meowww.AsukaMeow.dialog;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.database.IMongoStorable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.util.Consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @see <href a="https://www.spigotmc.org/threads/chat-click-event-callback-chatcomponent.238380/">Spigot Example</>
 * @see <href a="https://gist.github.com/rodel77/b6966471d51d5176d0da9bd0120d0a4b">Gist: Spigot Chat Click Callback</href>
 */
public class DialogManager implements IMongoStorable {

    private MongoCollection dialogCol;
    private static final Map<UUID, Consumer<Player>> callbacks = new HashMap<>();
    private Map<UUID, Dialog> dialogMap = new HashMap<>();

    @Override
    public void initCollection(MongoDatabase db) {
        this.dialogCol = db.getCollection("dialog");
    }

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

    public Map<UUID, Dialog> getDialogMap () {
        return dialogMap;
    }

    public Dialog getDialog(UUID uuid) {
        return dialogMap.get(uuid);
    }

    public void loadDialog (UUID uuid) {
        Object obj = dialogCol.find(new Document("uuid", uuid.toString())).first();
        Dialog d = Dialog.fromDocument((Document) obj);
        if (dialogMap.containsKey(d.getUUID()))
            dialogMap.replace(d.getUUID(), d);
        else
            dialogMap.put(d.getUUID(), d);
    }

    public void loadDialogs () {
        Map<UUID, Dialog> loadedDialogMap = new HashMap<>();
        Dialog d;
        MongoCursor<Document> cursor = dialogCol.find().cursor();
        while(cursor.hasNext()) {
            try {
                d = Dialog.fromDocument(cursor.next());
                loadedDialogMap.put(d.getUUID(), d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dialogMap = loadedDialogMap;
        AsukaMeow.INSTANCE.getLogger().info("Loaded " + dialogMap.size() + " dialogs!");
    }

    public void updateDialog () {
        dialogMap.forEach((uuid, dialog) -> {
            dialogCol.replaceOne(
                    new Document("uuid", uuid.toString()),
                    Dialog.toDocument(dialog),
                    new ReplaceOptions().upsert(true));
        });
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