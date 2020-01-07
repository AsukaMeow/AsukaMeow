package at.meowww.AsukaMeow.item.feature;

import at.meowww.AsukaMeow.util.Utils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class FeatureTeleport implements IFeature {

    public static final String name ="TELEPORT";
    public static final String lowerName = name.toLowerCase();
    protected Location location;
    protected int cooldown;
    protected Date nextUseDatetime;

    public FeatureTeleport () {}

    public FeatureTeleport(Location location, int cooldown, Date nextUseDatetime) {
        this.location = location;
        this.cooldown = cooldown;
        this.nextUseDatetime = nextUseDatetime;
    }

    @Override
    public <T extends Event> void updateLore(ItemStack item, T event) {
        updateLore(item);
    }

    @Override
    public void updateLore(ItemStack item) {
        if (new Date().before(nextUseDatetime)) {
            String leftString = "剩餘冷卻時間："
                    + Utils.getDatetimeDiffInt(
                            new Date(), nextUseDatetime, TimeUnit.SECONDS)
                    + "秒";
            List<String> lores = item.getLore();
            for(int i = 0; i < lores.size(); ++i) {
                if (lores.get(i).contains("剩餘冷卻時間：")) {
                    lores.set(i, leftString);
                    item.setLore(lores);
                    return;
                }
            }
            lores.add(0, leftString);
            item.setLore(lores);
        }
    }

}
