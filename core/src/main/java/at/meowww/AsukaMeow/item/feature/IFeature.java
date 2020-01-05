package at.meowww.AsukaMeow.item.feature;

import at.meowww.AsukaMeow.item.AsukaItem;
import org.bukkit.event.Event;

public interface IFeature {

    String getName();

    <T extends Event> void trigger (AsukaItem item, T event);

    <T extends Event> void updateLore (AsukaItem item, T event);

    void updateLore (AsukaItem item);

}
