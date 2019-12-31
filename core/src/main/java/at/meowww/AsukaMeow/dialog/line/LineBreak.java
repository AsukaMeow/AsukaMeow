package at.meowww.AsukaMeow.dialog.line;

import net.md_5.bungee.api.chat.BaseComponent;

public class LineBreak {

    public static BaseComponent create () {
        return new LineBuilder("").create();
    }

}
