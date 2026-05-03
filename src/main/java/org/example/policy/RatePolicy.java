package org.example.policy;

import org.example.Money;
import org.example.Phone;

public interface RatePolicy {
    public Money calculateFee(Phone phone);
}
