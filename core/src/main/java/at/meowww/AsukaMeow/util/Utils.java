package at.meowww.AsukaMeow.util;

import java.lang.Character.UnicodeBlock;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static final HashMap<UnicodeBlock, Double> CHAR_SIZE_MAP = new HashMap<>();
    private static final DateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH");

    static {
        CHAR_SIZE_MAP.put(UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS, 1.0D);
        CHAR_SIZE_MAP.put(UnicodeBlock.BASIC_LATIN, 1.0D);
        CHAR_SIZE_MAP.put(UnicodeBlock.DINGBATS, 2.25D);
        CHAR_SIZE_MAP.put(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION, 2.25D);
        CHAR_SIZE_MAP.put(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS, 2.25D);
    }

    public static long getDatetimeDiff (Date first, Date second, TimeUnit timeUnit) {
        return timeUnit.convert(second.getTime() - first.getTime(), TimeUnit.MILLISECONDS);
    }

    public static int getDatetimeDiffInt (Date first, Date second, TimeUnit timeUnit) {
        return Math.toIntExact(getDatetimeDiff(first, second, timeUnit));
    }


    public static double charSize (char c) {
        return getSize(UnicodeBlock.of(c));
    }

    public static double stringCharSize (String s) {
        double size = 0.0D;
        for (int i = 0; i < s.length(); ++i) {
            int codePoint = s.codePointAt(i);
            size += getSize(UnicodeBlock.of(codePoint));
        }
        return size;
    }

    private static double getSize (UnicodeBlock block) {
        return CHAR_SIZE_MAP.containsKey(block) ? CHAR_SIZE_MAP.get(block) : 1.0D;
    }

    public static Date stringToDate (String string) {
        try {
            return dt.parse(string);
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToString (Date date) {
        return dt.format(date);
    }

}
