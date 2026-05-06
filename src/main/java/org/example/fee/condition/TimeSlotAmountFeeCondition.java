package org.example.fee.condition;

import org.example.Call;
import org.example.TimeRange;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotAmountFeeCondition implements FeeCondition {
    private final Duration from;
    private final Duration to;

    public TimeSlotAmountFeeCondition(Duration from, Duration to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public List<TimeRange> findIntervals(Call call) {
        List<TimeRange> result = new ArrayList<>();
        Duration callDuration = call.getDuration();

        if (from.compareTo(callDuration) >= 0) {
            return result;
        }

        LocalDateTime intervalFrom = call.getFrom().plus(from);
        LocalDateTime intervalTo;

        if (to.compareTo(callDuration) >= 0) {
            intervalTo = call.getTo();
        } else {
            intervalTo = call.getFrom().plus(to);
        }

        if (intervalFrom.isBefore(intervalTo)) {
            result.add(new TimeRange(LocalTime.from(intervalFrom), LocalTime.from(intervalTo)));
        }

        return result;
    }

}