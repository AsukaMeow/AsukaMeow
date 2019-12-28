package at.meowww.AsukaMeow.dialog.line;

import at.meowww.AsukaMeow.dialog.AlignOption;
import at.meowww.AsukaMeow.dialog.DialogManager;
import at.meowww.AsukaMeow.util.Utils;
import com.google.common.base.Strings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * For building a line like BaseComponents
 *
 * This class is references {@link net.md_5.bungee.api.chat.ComponentBuilder}.
 * And it responsible to create a series components performs align, color, bold
 * , clickEvent, hoverEvent, and so on.
 *
 * @see at.meowww.AsukaMeow.util.Utils#charSize(char)
 * @see at.meowww.AsukaMeow.util.Utils#stringCharSize(String)
 * @author clooooode
 * @since 0.0.3-SNAPSHOT
 */
public class LineBuilder {

    protected char fill_char = ' ';
    protected static final double CHAR_LIMIT = 28D;
    protected static final double BOLD_CHAR_LIMIT = 26D;
    protected static final double LIMIT = 25D;

    private boolean isLocked;
    private double totalCharLength;
    private BaseComponent current;
    private final List<BaseComponent> parts = new ArrayList();

    public LineBuilder(String text) {
        this.isLocked = false;
        this.current = new TextComponent(text);
        this.totalCharLength = Utils.stringCharSize(text);
    }

    public LineBuilder bold(boolean value) {
        if (!isLocked)
            current.setBold(value);
        return this;
    }

    public LineBuilder color(ChatColor color) {
        if (!isLocked)
            current.setColor(color);
        return this;
    }

    /**
     * Register the click event's consumer to DialogManager
     *
     * {@link DialogManager#registerCallback} shows the detail how this works.
     * It's just a callback with tricky command send.
     * @param consumer
     * @return
     */
    public LineBuilder click(Consumer<Player> consumer) {
        current.setClickEvent(DialogManager.registerCallback(consumer));
        return this;
    }

    public LineBuilder hover(BaseComponent ... components) {
        current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, components));
        return this;
    }

    public LineBuilder append(String text) {
        return append(new TextComponent(text));
    }

    public LineBuilder append(BaseComponent component) {
        return append(component, ComponentBuilder.FormatRetention.FORMATTING);
    }

    /**
     * Append a component with format retention
     *
     * In order to perform line limit, only the length after append can still
     * under LINE_LIMIT can be append.
     *
     * And this is the only method will increases totalCharLength variable.
     *
     * @see at.meowww.AsukaMeow.util.Utils#stringCharSize(String)
     * @param component
     * @param retention
     * @return
     */
    public LineBuilder append(BaseComponent component, ComponentBuilder.FormatRetention retention) {
        if (isLocked || totalCharLength + Utils.stringCharSize(component.toPlainText()) > CHAR_LIMIT)
            return this;
        totalCharLength += Utils.stringCharSize(component.toPlainText());
        parts.add(current);
        BaseComponent previous = current;
        current = component.duplicate();
        current.copyFormatting(previous, retention, false);
        return this;
    }

    /**
     * Insert fill chars into a line to make alignment
     *
     * Fill char would be space by default, currently there is no any other way
     *  to change it.
     *
     *  This method remains private because it would insert series component to
     * make a alignment, so it should only been call once at very last call in
     * LineBuilder, which is {@link #create} method.
     *
     * @see at.meowww.AsukaMeow.util.Utils#charSize(char)
     * @param option
     * @return
     */
    private BaseComponent align(AlignOption option) {
        TextComponent results = new TextComponent();
        int needCount = (int) ((CHAR_LIMIT - totalCharLength) / Utils.charSize(fill_char));
        String padStr;
        switch (option) {
            case SPLIT:
                padStr = Strings.padEnd("", needCount / (parts.size() + 2), fill_char);
                results.addExtra(padStr);
                for (BaseComponent bc : parts) {
                    results.addExtra(bc);
                    results.addExtra(padStr);
                }
                results.addExtra(current);
                results.addExtra(padStr);
                break;
            case CENTER:
                padStr = Strings.padEnd("", needCount / 2, fill_char);
                results.addExtra(padStr);
                for (BaseComponent bc : parts)
                    results.addExtra(bc);
                results.addExtra(current);
                results.addExtra(padStr);
                break;
            case RIGHT:
                padStr = Strings.padEnd("", needCount, fill_char);
                results.addExtra(padStr);
                for (BaseComponent bc : parts)
                    results.addExtra(bc);
                results.addExtra(current);
                break;
            case LEFT:
            default:
                padStr = Strings.padEnd("", needCount, fill_char);
                for (BaseComponent bc : parts)
                    results.addExtra(bc);
                results.addExtra(current);
                results.addExtra(padStr);
                break;
        }
        return results;
    }

    public BaseComponent create(AlignOption option) {
        isLocked = true;
        BaseComponent bc = align(option);
        if (bc.toPlainText().length() < CHAR_LIMIT)
            bc.addExtra("\n");
        return bc;
    }

    public BaseComponent create() {
        isLocked = true;
        BaseComponent bc = new TextComponent(parts.toArray(new BaseComponent[parts.size()]));
        bc.addExtra(current);
        if (bc.toPlainText().length() < CHAR_LIMIT)
            bc.addExtra("\n");
        return bc;
    }

}
