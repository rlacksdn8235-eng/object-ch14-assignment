package org.example.fee.condition;

import org.example.Call;
import org.example.TimeRange;

import java.util.List;

public class TimeFixedAmountFeeCondition implements FeeCondition {
    @Override
    public List<TimeRange> findIntervals(Call call) {
        return List.of(new TimeRange(call.getFrom().toLocalTime(), call.getTo().toLocalTime()));
    }
}
