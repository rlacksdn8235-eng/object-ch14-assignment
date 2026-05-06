package org.example.fee.condition;

import org.example.Call;
import org.example.TimeRange;

import java.util.List;

public interface FeeCondition {
    List<TimeRange> findIntervals(Call call);
}
