import java.util.Calendar;
import org.bukkit.event.Listener;

public class Tools implements Listener {
    public Bingo plugin = Bingo.plugin;

    public static String formatDuration(long d, boolean precise) {
        if (precise) {
            long time = d;
            long seconds = time % 60L;
            time /= 60L;
            long minutes = time % 60L;
            long hours = time / 60L;
            return String.format("%02d:%02d:%02d", new Object[] { Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds) });
        }
        long minutes2 = d / 60L;
        return String.valueOf(String.valueOf(String.valueOf(minutes2))) + " minute" + ((minutes2 != 1L) ? "s" : "");
    }

    public static String formatDuration(Calendar t1, Calendar t2, boolean precise) {
        return formatDuration(getDuration(t1, t2), precise);
    }

    public static long getDuration(Calendar t1, Calendar t2) {
        return (t2.getTimeInMillis() - t1.getTimeInMillis()) / 1000L;
    }

    public static Boolean stringToBoolean(String s) {
        if ("true".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s) || "y".equalsIgnoreCase(s) || "1".equals(s))
            return Boolean.valueOf(true);
        if ("false".equalsIgnoreCase(s) || "off".equalsIgnoreCase(s) || "no".equalsIgnoreCase(s) || "n".equalsIgnoreCase(s) || "0".equals(s))
            return Boolean.valueOf(false);
        return null;
    }
}