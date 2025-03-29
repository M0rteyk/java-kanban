package file;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH.mm");

    public static String formatForOutput(LocalDateTime time) {
        return time != null ? time.format(FORMATTER) : "не указано";
    }

    public static String formatForCsv(LocalDateTime time) {
        return time != null ? time.format(FORMATTER) : "";
    }

    public static LocalDateTime parseFromCsv(String timeString) {
        return timeString.isEmpty() ? null :
                LocalDateTime.parse(timeString, FORMATTER);
    }
}
