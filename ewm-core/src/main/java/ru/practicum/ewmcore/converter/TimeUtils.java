package ru.practicum.ewmcore.converter;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtils {
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public Timestamp stringToTimestamp(String entity) {
        final LocalDateTime dateTime = LocalDateTime.parse(entity, FORMATTER);
        final Timestamp model = Timestamp.valueOf(dateTime);
        return model;
    }

    public String timestampToString(Timestamp entity) {
        final LocalDateTime dateTime = entity.toLocalDateTime();
        final String model = dateTime.format(FORMATTER);
        return model;
    }

    public Timestamp now() {
        return Timestamp.from(Instant.now());
    }
}
