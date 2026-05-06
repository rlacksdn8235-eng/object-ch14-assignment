package org.example;

import java.time.Duration;
import java.time.LocalTime;

public record TimeRange(LocalTime from, LocalTime to) {

    public Duration getDuration() {
        return Duration.between(from, to);
    }
}
