package org.example.calculator;

import org.example.Call;
import org.example.Money;

import java.time.Duration;

public class TimeSlotAmountCalculator implements Calculator {
    private final Duration from;
    private final Duration to;
    private final Money amount;

    public TimeSlotAmountCalculator(Duration from, Duration to, Money amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public Money calculate(Call call, Duration seconds) {
        Duration callDuration = call.getDuration();
        Duration actualFrom = callDuration;
        if(from.compareTo(callDuration) < 0) {
            actualFrom = from;
        }

        Duration actualTo = callDuration;
        if (to.compareTo(callDuration) < 0){
           actualTo = to;
        }

        Duration duration = actualTo.minus(actualFrom);

        if (duration.isZero() || duration.isNegative()) {
            return Money.ZERO;
        }

        long units = duration.getSeconds() / seconds.getSeconds();

        return amount.times(units);
    }
}