package org.example;

public class Money {
    public static final Money ZERO = new Money(0);

    private final long amount;

    public Money(long amount) {
        this.amount = amount;
    }

    public static Money wons(long amount) {
        return new Money(amount);
    }

    public Money plus(Money other) {
        return new Money(this.amount + other.amount);
    }

    public Money minus(Money other) {
        return new Money(this.amount - other.amount);
    }

    public Money times(double percent) {
        return new Money((long)(this.amount * percent));
    }

    public long getAmount() {
        return amount;
    }
}