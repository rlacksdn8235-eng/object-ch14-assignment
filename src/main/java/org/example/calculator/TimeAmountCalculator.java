package org.example.calculator;

import org.example.Call;
import org.example.Money;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeAmountCalculator implements Calculator{
    private final LocalTime timeFrom;
    private final LocalTime timeTo;
    private final Money amount;

    public TimeAmountCalculator(LocalTime timeFrom, LocalTime timeTo, Money amount) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.amount = amount;
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
        LocalDateTime from = calculateFrom(callFrom);
        LocalDateTime to = calculateTo(callFrom, callTo);

        if (!from.isBefore(to)) {
            return Money.ZERO;
        }

        return calculateAmount(from, to, seconds);
    }

    private LocalDateTime calculateFrom(LocalDateTime callFrom) {
        LocalDateTime policyFrom = LocalDateTime.of(callFrom.toLocalDate(), timeFrom);

        if (policyFrom.isAfter(callFrom)) {
            return policyFrom;
        }

        return callFrom;
    }

    private LocalDateTime calculateTo(LocalDateTime callFrom, LocalDateTime callTo) {
        LocalDateTime policyTo = LocalDateTime.of(callFrom.toLocalDate(), timeTo);

        if (policyTo.isBefore(callTo)) {
            return policyTo;
        }

        return callTo;
    }

    private Money calculateAmount(
            LocalDateTime from,
            LocalDateTime to,
            Duration seconds
    ) {
        Duration duration = Duration.between(from, to);
        return amount.times((double) duration.getSeconds() / seconds.getSeconds());
    }
}