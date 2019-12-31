package at.meowww.AsukaMeow.dialog.page;

import at.meowww.AsukaMeow.dialog.line.LineBreak;
import net.md_5.bungee.api.chat.BaseComponent;

public class PageBuilder {

    public static BaseComponent[] emptyPage () {
        BaseComponent[] pages = new BaseComponent[Page.LINE_LIMIT];
        for (int i = 0; i < Page.LINE_LIMIT; ++i)
            pages[i] = LineBreak.create();
        return pages;
    }

}
