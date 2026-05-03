package org.example.calculator;

import org.example.Call;
import org.example.Money;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

public class DayOfWeekAmountCalculator implements Calculator {
    private final Money weekDayAmount;
    private final Money holidayAmount;

    public DayOfWeekAmountCalculator(Money weekDayAmount, Money holidayAmount) {
        this.weekDayAmount = weekDayAmount;
        this.holidayAmount = holidayAmount;
    }

    @Override
    public Money calculate(Call call, Duration seconds) {
        Money result = Money.ZERO;
        LocalDateTime current = call.getFrom();

        while (current.isBefore(call.getTo())) {
            LocalDateTime splitTo = calculateSplitTo(current, call.getTo());

            result = result.plus(calculateSameDay(current, splitTo, seconds));

            current = splitTo;
        }

        return result;
    }

    private LocalDateTime calculateSplitTo(LocalDateTime current, LocalDateTime callTo) {
        LocalDateTime nextMidnight = current.toLocalDate()
                .plusDays(1)
                .atStartOfDay();

        if (nextMidnight.isBefore(callTo)) {
            return nextMidnight;
        }

        return callTo;
    }

    private Money calculateSameDay(
            LocalDateTime callFrom,
            LocalDateTime callTo,
            Duration seconds
    ) {
        Duration duration = Duration.between(callFrom, callTo);
        Money amount = calculateAmountByDay(callFrom);

        return calculateAmount(duration, amount, seconds);
    }

    private Money calculateAmountByDay(LocalDateTime dateTime) {
        if (isWeekDay(dateTime)) {
            return weekDayAmount;
        }

        return holidayAmount;
    }

    private Money calculateAmount(
            Duration duration,
            Money amount,
            Duration seconds
    ) {
        long units = duration.getSeconds() / seconds.getSeconds();

        return amount.times(units);
    }

    private boolean isWeekDay(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();

        return day != DayOfWeek.SATURDAY
                && day != DayOfWeek.SUNDAY;
    }
}