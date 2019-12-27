package at.meowww.AsukaMeow.dialog.line;

import at.meowww.AsukaMeow.dialog.page.Page;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class EmptyLine extends Line {

    public EmptyLine() {
        super(new TextComponent("\n"));
    }

    @Override
    protected BaseComponent[] finalizeComponents () {
        return this.components;
    }

    @Override
    public BaseComponent toLineComponent () {
        return finalizeComponents()[0];
    }

    public static Line[] emptyPage () {
        Line[] lines = new Line[Page.LINE_LIMIT];
        for (int i = 0; i < Page.LINE_LIMIT; ++i) {
            lines[i] = new EmptyLine();
        }
        return lines;
    }
}
