package at.meowww.AsukaMeow.dialog;

import at.meowww.AsukaMeow.dialog.page.Page;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bson.Document;

import java.util.UUID;

public class Dialog {

    private final UUID uuid;
    private DialogType type;
    private TextComponent title;
    private Page[] pages;

    public Dialog (UUID uuid, DialogType type, TextComponent title, Page ... pages) {
        this.uuid = uuid;
        this.type = type;
        this.title = title;
        this.pages = pages;
    }

    public Dialog (DialogType type, TextComponent title, Page ... pages) {
        this(UUID.randomUUID(), type, title, pages);
    }

    public UUID getUUID () {
        return uuid;
    }

    public TextComponent getTitle () {
        return title;
    }

    public BaseComponent[] toComponents () {
        BaseComponent[] components = new BaseComponent[pages.length];
        for (int i = 0; i < components.length; ++i)
            components[i] = pages[i].toComponent();
        return components;
    }

    public static Document toDocument (Dialog dialog) {
        Document pageDoc = new Document();
        for (int i = 0; i < dialog.pages.length; ++i)
            pageDoc.append(String.valueOf(i), Page.toDocument(dialog.pages[i]));

        return new Document("uuid", dialog.uuid.toString())
                .append("type", dialog.type)
                .append("title", Document.parse(ComponentSerializer.toString(dialog.title)))
                .append("pages", pageDoc);
    }

    public static Dialog fromDocument (Document document) {
        UUID uuid = UUID.fromString(document.getString("uuid"));
        DialogType type = DialogType.valueOf(document.get("type", DialogType.UNSET.toString()));
        TextComponent title = new TextComponent(
                ComponentSerializer.parse(
                        ((Document) document.get("title")).toJson())
        );

        Document pageDoc = (Document) document.get("pages");
        Page[] pages = new Page[pageDoc.size()];
        for (int i = 0; i < pageDoc.size(); ++i)
            pages[i] = Page.fromDocument((Document) pageDoc.get(
                    String.valueOf(i))
            );

        return new Dialog(uuid, type, title, pages);
    }

}
