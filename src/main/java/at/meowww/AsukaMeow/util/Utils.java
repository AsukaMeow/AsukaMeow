package at.meowww.AsukaMeow.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static long getDatetimeDiff (Date first, Date second, TimeUnit timeUnit) {
        return timeUnit.convert(second.getTime() - first.getTime(), TimeUnit.MILLISECONDS);
    }

    public static int getDatetimeDiffInt (Date first, Date second, TimeUnit timeUnit) {
        return Math.toIntExact(getDatetimeDiff(first, second, timeUnit));
    }
}
