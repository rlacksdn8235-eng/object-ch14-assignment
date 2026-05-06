package org.example.fee;

import org.example.Call;
import org.example.Money;
import org.example.TimeRange;
import org.example.fee.condition.FeeCondition;

import java.time.Duration;
import java.util.List;

public class FeeRule {
    private final FeeCondition feeCondition; // 조건
    private final Money money; // 초당금액
    private final Duration seconds; // 초

    public FeeRule(FeeCondition feeConditions, Money money, Duration seconds) {
        this.feeCondition = feeConditions;
        this.money = money;
        this.seconds = seconds;
    }

    public Money calculateFee(Call call) {
        // 요금제별 from to 만 날짜별로 잘 나눠 주면 알아서 계산
        // list는 condition에 맞게 날짜별로 나뉜 from to
        // condition의 조건에 맞지 않는 시간은 포함되면 안됨
        List<TimeRange> intervals = feeCondition.findIntervals(call);
        Money result = Money.ZERO;
        for(TimeRange interval : intervals) {
            result = result.plus(calculateAmount(interval));
        }
        return result;
    }

    private Money calculateAmount(TimeRange interval) {
        return money.times((double) interval.getDuration().getSeconds() / seconds.getSeconds());
    }
}
