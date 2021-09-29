package com.ee.offer;

import java.math.BigDecimal;
import java.util.Objects;

public class Discount {
    public final BigDecimal amount;

    public Discount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return Objects.equals(amount, discount.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "Discount{" +
                "amount=" + amount +
                '}';
    }
}
