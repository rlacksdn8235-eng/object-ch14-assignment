package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record DateTimeRange(LocalDateTime from, LocalDateTime to) {

    public Duration getDuration() {
        return Duration.between(from, to);
    }

    public TimeRange toTimeRange() {
        return new TimeRange(LocalTime.from(from), LocalTime.from(to));
    }
}
