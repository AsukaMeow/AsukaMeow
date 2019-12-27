package at.meowww.AsukaMeow.dialog.page;

import at.meowww.AsukaMeow.dialog.line.EmptyLine;
import at.meowww.AsukaMeow.dialog.line.Line;
import at.meowww.AsukaMeow.dialog.line.LinesOverLimitException;

public class PageOption extends Page {

    PageOption(Line[] lines) {
        super(lines);
    }

    public static PageOption build(Line ... options) throws LinesOverLimitException {
        if (options.length > Page.LINE_LIMIT)
            throw new LinesOverLimitException();
        Line[] lines = EmptyLine.emptyPage();
        System.arraycopy(options, 0, lines, LINE_LIMIT - options.length, options.length);
        return new PageOption(lines);
    }

    public static PageOption build(Line[] text, Line[] options) throws LinesOverLimitException {
        if (text.length + options.length > Page.LINE_LIMIT)
            throw new LinesOverLimitException();
        Line[] lines = EmptyLine.emptyPage();
        System.arraycopy(text, 0, lines, 0, text.length);
        System.arraycopy(options, 0, lines, 14 - options.length, options.length);
        return new PageOption(lines);
    }

}
