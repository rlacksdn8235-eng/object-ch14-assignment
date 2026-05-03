package org.example.calculator;

import org.example.Call;
import org.example.Money;

import java.time.Duration;

public interface Calculator {
    public Money calculate(Call call, Duration seconds);
}
