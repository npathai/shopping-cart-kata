package com.ee.offer.product;

import com.ee.offer.Discount;
import com.ee.product.Product;

import java.util.Optional;

public interface ProductOffer {
    Optional<Discount> apply(Product product, int quantity);
}
