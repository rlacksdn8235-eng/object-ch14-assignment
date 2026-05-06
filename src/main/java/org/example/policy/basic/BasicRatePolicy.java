package org.example.policy.basic;

import org.example.Call;
import org.example.Money;
import org.example.Phone;
import org.example.fee.FeeRule;
import org.example.policy.RatePolicy;

import java.util.List;

public class BasicRatePolicy implements RatePolicy {
    private final List<FeeRule> feeRules;

    public BasicRatePolicy(List<FeeRule> feeRules) {
        this.feeRules = feeRules;
    }

    @Override
    public Money calculateFee(Phone phone) {
        Money result = Money.ZERO;
        for (Call call : phone.getCalls()) {
            result = result.plus(calculateCallFee(call));
        }
        return result;
    }

    private Money calculateCallFee(Call call) {
        Money amount = Money.ZERO;
        for(FeeRule feeRule : feeRules) {
            amount = amount.plus(feeRule.calculateFee(call));
        }
        return amount;
    };
}
