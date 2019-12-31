package at.meowww.AsukaMeow.dialog.page;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bson.Document;

import javax.validation.constraints.Size;

public class Page {

    public static final int LINE_LIMIT = 14;

    @Size(max=LINE_LIMIT)
    private BaseComponent[] lines;

    public Page (BaseComponent ... lines) {
        this.lines = lines;
    }

    public BaseComponent toComponent () {
        TextComponent pageText = new TextComponent();
        for (BaseComponent bc : lines)
            pageText.addExtra(bc);
        return pageText;
    }

    public static Document toDocument (Page page) {
        Document lineDocs = new Document();
        for (int i = 0; i < page.lines.length; ++i)
            lineDocs.append(
                    String.valueOf(i),
                    Document.parse(ComponentSerializer.toString(page.lines[i]))
            );
        return lineDocs;
    }

    public static Page fromDocument (Document document) {
        BaseComponent[] lines = new BaseComponent[document.size()];
        for (int i = 0; i < document.size(); ++i)
            lines[i] = new TextComponent(
                    ComponentSerializer.parse(
                            ((Document) document.get(String.valueOf(i))).toJson()
                    )
            );
        return new Page(lines);
    }

}
