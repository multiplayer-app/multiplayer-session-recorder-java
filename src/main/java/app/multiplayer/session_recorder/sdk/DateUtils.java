package app.multiplayer.session_recorder.sdk;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String getFormattedDate(long epochMillis) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault());

        return formatter.format(Instant.ofEpochMilli(epochMillis));
    }
}