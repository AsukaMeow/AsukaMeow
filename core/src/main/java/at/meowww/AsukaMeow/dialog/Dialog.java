package at.meowww.AsukaMeow.dialog;

import at.meowww.AsukaMeow.dialog.page.Page;

import java.util.ArrayList;
import java.util.List;

public class Dialog {

    private Page[] pages;

    public Dialog (Page ... pages) {
        this.pages = pages;
    }

    public List<String> toList () {
        ArrayList<String> contents = new ArrayList<>();
        for (Page p : pages) {
            contents.add(p.toComponent().toLegacyText());
        }
        return contents;
    }

}
