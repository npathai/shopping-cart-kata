package com.ee.offer.cart;

import com.ee.cart.dto.ShoppingCartDto;
import com.ee.offer.Discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class Flat20PercentOffOffer implements CartOffer {

    @Override
    public Optional<Discount> apply(ShoppingCartDto shoppingCart) {
        if (shoppingCart.totalExcludingTax.compareTo(BigDecimal.valueOf(500)) < 0) {
            return Optional.empty();
        }

        return Optional.of(new Discount(shoppingCart.totalExcludingTax.multiply(BigDecimal.valueOf(20))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
    }
}
