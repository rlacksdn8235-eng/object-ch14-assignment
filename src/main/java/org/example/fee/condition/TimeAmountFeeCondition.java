package org.example.fee.condition;

import org.example.Call;
import org.example.DateTimeRange;
import org.example.TimeRange;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeAmountFeeCondition implements FeeCondition {
    private final TimeRange timeRange;

    public TimeAmountFeeCondition(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    // 시간대별 방식 A시~B시, B시~C시 / 0 ~ 19, 19 ~ 24
    // Call의 TimeRange의 from to 18~20 시라면 18~19, 19~20 으로 분리
    // conditionTimeRange는 0~19 거나 19~24
    // call의 from to 가 conditionTimeRange 의 from to 와 비교
    // 둘중 더 큰 from 더 작은 to
    @Override
    public List<TimeRange> findIntervals(Call call) {
        List<TimeRange> result = new ArrayList<>();
        LocalDateTime current = call.getFrom();
        while (current.isBefore(call.getTo())) {
            DateTimeRange splitRange = calculateSplitRange(current, call.getTo());
            TimeRange range = calculateRange(splitRange);
            result.add(range);
            current = splitRange.to();
        }
        return result;
    }

    private DateTimeRange calculateSplitRange(LocalDateTime current, LocalDateTime callTo) {
        LocalDateTime nextMidnight = current.toLocalDate()
                .plusDays(1)
                .atStartOfDay();
        LocalDateTime to = callTo;
        if (nextMidnight.isBefore(callTo)) {
            to = nextMidnight;
        }
        return new DateTimeRange(current, to);
    }

    private TimeRange calculateRange(DateTimeRange splitRange) {
        LocalTime from = calculateFrom(splitRange);
        LocalTime to = calculateTo(splitRange);
        return new TimeRange(from, to);
    }

    private LocalTime calculateFrom(DateTimeRange splitRange) {
        if (timeRange.from().isAfter(LocalTime.from(splitRange.from()))) {
            return timeRange.from();
        }
        return LocalTime.from(splitRange.from());
    }

    private LocalTime calculateTo(DateTimeRange splitRange) {
        if (timeRange.to().isBefore(LocalTime.from(splitRange.to()))) {
            return timeRange.to();
        }
        return LocalTime.from(splitRange.to());
    }

}
