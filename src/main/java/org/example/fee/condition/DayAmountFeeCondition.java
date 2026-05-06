package org.example.fee.condition;

import org.example.Call;
import org.example.DateTimeRange;
import org.example.TimeRange;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DayAmountFeeCondition implements FeeCondition {
    private final List<DayOfWeek> dayOfWeeks;

    public DayAmountFeeCondition(List<DayOfWeek> dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }

    @Override
    public List<TimeRange> findIntervals(Call call) {
        List<TimeRange> result = new ArrayList<>();
        LocalDateTime current = call.getFrom();
        while (current.isBefore(call.getTo())) {
            DateTimeRange splitRange = calculateSplitRange(current, call.getTo());
            if (isSatisfied(splitRange)) {
                result.add(splitRange.toTimeRange());
            }
            current = splitRange.to().plusSeconds(1);
        }
        return result;
    }

    private DateTimeRange calculateSplitRange(LocalDateTime current, LocalDateTime callTo) {
        LocalDateTime nextMidnight = current.toLocalDate()
                .plusDays(1)
                .atStartOfDay();
        LocalDateTime to = callTo;
        if (nextMidnight.isBefore(callTo)) {
            to = nextMidnight.minusSeconds(1);
        }
        return new DateTimeRange(current, to);
    }

    private boolean isSatisfied(DateTimeRange range) {
        DayOfWeek dayOfWeek = range.from().getDayOfWeek();
        return dayOfWeeks.contains(dayOfWeek);
    }
}
