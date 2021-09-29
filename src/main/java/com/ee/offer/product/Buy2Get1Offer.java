package com.ee.offer.product;

import com.ee.offer.Discount;
import com.ee.product.Product;

import java.math.BigDecimal;
import java.util.Optional;

import static com.ee.util.Preconditions.checkArgument;

public class Buy2Get1Offer implements ProductOffer {

    @Override
    public Optional<Discount> apply(Product product, int quantity) {
        checkArgument(quantity > 0, "Quantity should be greater than 0");

        int applicableOffers = quantity / 3;

        if (applicableOffers == 0) {
            return Optional.empty();
        }
        return Optional.of(new Discount(product.unitPrice.multiply(BigDecimal.valueOf(applicableOffers))));
    }
}
