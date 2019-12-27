package at.meowww.AsukaMeow.dialog.line;


import at.meowww.AsukaMeow.dialog.AlignOption;
import at.meowww.AsukaMeow.util.Utils;
import com.google.common.base.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class Line {

    protected char fill_char = ' ';
    protected static final double CHAR_LIMIT = 28D;
    protected static final double BOLD_CHAR_LIMIT = 26D;
    protected static final double LIMIT = 25D;

    protected AlignOption option;
    protected BaseComponent[] components;

    public Line(BaseComponent component) {
        this(new BaseComponent[] {component});
    }

    public Line(BaseComponent[] components) {
        this(components, AlignOption.LEFT);
    }

    public Line(BaseComponent component, AlignOption option) {
        this(new BaseComponent[] {component}, option);
    }

    public Line(BaseComponent[] components, AlignOption option) {
        this.components = components;
        this.option = option;
    }

    public Line align(AlignOption option) {
        this.option = option;
        return this;
    }

    protected BaseComponent[] finalizeComponents() {
        double textLength = 0.0D;
        for (BaseComponent c : components) {
            textLength += Utils.stringCharSize(c.toPlainText());
        }

        BaseComponent[] finalComponents;
        int spaceCount = (int) ((CHAR_LIMIT - textLength) / Utils.charSize(fill_char));
        ComponentBuilder cb;
        switch (option) {
            case SPLIT:
                int spacePerSplit = spaceCount / (components.length + 1);
                cb = new ComponentBuilder(Strings.padEnd("", spacePerSplit, fill_char))
                        .color(ChatColor.BLACK);
                for (BaseComponent c : components) {
                    cb.append(c).append(Strings.padEnd("", spacePerSplit, fill_char));
                }
                finalComponents = cb.create();
                break;
            case CENTER:
                int spaceAtSide = spaceCount / 2;
                cb = new ComponentBuilder(Strings.padEnd("", spaceAtSide, fill_char))
                        .color(ChatColor.BLACK);
                for (BaseComponent c : components) {
                    cb.append(c);
                }
                cb.append(Strings.padEnd("", spaceAtSide, fill_char));
                finalComponents = cb.create();
                break;
            case RIGHT:
                cb = new ComponentBuilder(Strings.padEnd("", spaceCount, fill_char))
                        .color(ChatColor.BLACK);
                for (BaseComponent c : components) {
                    cb.append(c);
                }
                finalComponents = cb.create();
                break;
            case LEFT:
            default:
                cb = new ComponentBuilder("").color(ChatColor.BLACK);
                for (BaseComponent c : components) {
                    cb.append(c);
                }
                cb.append(Strings.padEnd("", spaceCount, fill_char));
                finalComponents = cb.create();
                break;
        }
        return finalComponents;
    }

    public BaseComponent toLineComponent () {
        TextComponent pageText = new TextComponent();
        for (BaseComponent c : finalizeComponents())
            pageText.addExtra(c);

        if (Utils.stringCharSize(pageText.toPlainText()) < CHAR_LIMIT)
            pageText.addExtra("\n");

        return pageText;
    }

}
