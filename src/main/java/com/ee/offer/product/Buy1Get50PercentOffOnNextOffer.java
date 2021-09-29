package com.ee.offer.product;

import com.ee.offer.Discount;
import com.ee.product.Product;

import java.math.BigDecimal;
import java.util.Optional;

import static com.ee.util.Preconditions.checkArgument;

public class Buy1Get50PercentOffOnNextOffer implements ProductOffer {

    @Override
    public Optional<Discount> apply(Product product, int quantity) {
        checkArgument(quantity > 0, "Cannot apply offer when quantity is 0");
        if (quantity == 1) {
            return Optional.empty();
        }

        int offerCount = quantity / 2;
        return Optional.of(new Discount(product.unitPrice.multiply(BigDecimal.valueOf(0.5))
                .multiply(BigDecimal.valueOf(offerCount))));
    }
}
