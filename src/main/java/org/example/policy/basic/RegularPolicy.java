package org.example.policy.basic;

import org.example.Call;
import org.example.Money;

import java.time.Duration;

public class RegularPolicy extends BasicRatePolicy {
    private final Duration seconds;
    private final Money amount;

    public RegularPolicy(Duration seconds, Money amount) {
        this.seconds = seconds;
        this.amount = amount;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times((double) call.getDuration().getSeconds() / seconds.getSeconds());
    }
}