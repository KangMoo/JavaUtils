package com.github.kangmoo.utils.common;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class DateUtil {
    private DateUtil() {
    }

    public static Optional<LocalDateTime> parseIso8601ToLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null) return Optional.empty();
        try {
            try {
                return Optional.of(LocalDateTime.parse(dateTimeStr));
            } catch (DateTimeParseException e1) {
                try {
                    return Optional.of(OffsetDateTime.parse(dateTimeStr).toLocalDateTime());
                } catch (DateTimeParseException e2) {
                    try {
                        return Optional.of(ZonedDateTime.parse(dateTimeStr).toLocalDateTime());
                    } catch (DateTimeParseException e3) {
                        try {
                            return Optional.of(LocalDate.parse(dateTimeStr).atStartOfDay());
                        } catch (DateTimeParseException e4) {
                            return Optional.of(LocalTime.parse(dateTimeStr).atDate(LocalDate.now()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Invalid date/time format. [{}]", dateTimeStr, e);
            return Optional.empty();
        }
    }
}
