package org.example;

import java.time.Duration;
import java.time.LocalDateTime;

public class Call {
    private DateTimeRange timeRange;

    public Call(DateTimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public Duration getDuration() {
        return timeRange.getDuration();
    }

    public LocalDateTime getFrom() {
        return timeRange.from();
    }

    public LocalDateTime getTo() { return timeRange.to(); }

}