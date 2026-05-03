package org.example.policy.basic;

import org.example.Call;
import org.example.Money;
import org.example.calculator.Calculator;

import java.time.Duration;
import java.util.List;

public class CompositeRatePolicy extends BasicRatePolicy {
    private final Duration seconds;
    private final List<? extends Calculator> calculators;

    public CompositeRatePolicy(Duration seconds, List<? extends Calculator> calculators) {
        this.seconds = seconds;
        this.calculators = calculators;
    }

    @Override
    public Money calculateCallFee(Call call) {
        Money result = Money.ZERO;
        for (Calculator calculator : calculators) {
            Money calculateAmount = calculator.calculate(call, seconds);
            result = result.plus(calculateAmount);
        }
        return result;
    }
}
