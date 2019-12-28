package at.meowww.AsukaMeow.dialog.page;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

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

}
