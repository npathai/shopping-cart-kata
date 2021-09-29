package com.ee.offer.cart;

import com.ee.cart.dto.ShoppingCartDto;
import com.ee.offer.Discount;

import java.util.Optional;

public interface CartOffer {
    Optional<Discount> apply(ShoppingCartDto shoppingCart);
}
