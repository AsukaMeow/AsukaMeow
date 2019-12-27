package at.meowww.AsukaMeow.dialog.page;

import at.meowww.AsukaMeow.dialog.line.Line;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import javax.validation.constraints.Size;

public class Page {

    public static final int LINE_LIMIT = 14;

    @Size(max=LINE_LIMIT)
    private Line[] lines;

    public Page(Line ... lines) {
        this.lines = lines;
    }

    public BaseComponent toComponent () {
        TextComponent pageText = new TextComponent();
        for (Line line : lines)
            pageText.addExtra(line.toLineComponent());
        return pageText;
    }

}
