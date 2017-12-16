package app.global.date

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtils {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

    static String convertToString(final LocalDateTime localDateTime) {
        localDateTime.format formatter
    }
}
