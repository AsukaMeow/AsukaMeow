package at.meowww.AsukaMeow.dialog;

import at.meowww.AsukaMeow.dialog.page.Page;
import net.md_5.bungee.api.chat.BaseComponent;

public class Dialog {

    private Page[] pages;

    public Dialog (Page ... pages) {
        this.pages = pages;
    }

    public BaseComponent[] toComponents () {
        BaseComponent[] components = new BaseComponent[pages.length];
        for (int i = 0; i < components.length; ++i)
            components[i] = pages[i].toComponent();
        return components;
    }

}
