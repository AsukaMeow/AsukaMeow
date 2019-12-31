package at.meowww.AsukaMeow.dialog.page;

import at.meowww.AsukaMeow.dialog.line.LinesOverLimitException;
import net.md_5.bungee.api.chat.BaseComponent;

public class PageOption extends Page {

    PageOption (BaseComponent ... lines) {
        super(lines);
    }

    public static PageOption build(BaseComponent ... options) throws LinesOverLimitException {
        if (options.length > Page.LINE_LIMIT)
            throw new LinesOverLimitException();
        BaseComponent[] lines = PageBuilder.emptyPage();
        System.arraycopy(options, 0, lines, LINE_LIMIT - options.length, options.length);
        return new PageOption(lines);
    }

    public static PageOption build(BaseComponent[] text, BaseComponent[] options) throws LinesOverLimitException {
        if (text.length + options.length > Page.LINE_LIMIT)
            throw new LinesOverLimitException();
        BaseComponent[] lines = PageBuilder.emptyPage();
        System.arraycopy(text, 0, lines, 0, text.length);
        System.arraycopy(options, 0, lines, 14 - options.length, options.length);
        return new PageOption(lines);
    }

}
