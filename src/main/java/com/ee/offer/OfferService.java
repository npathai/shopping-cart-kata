package com.ee.offer;

import com.ee.cart.dto.ShoppingCartDto;
import com.ee.product.Product;

import java.util.Optional;

public interface OfferService {
    Optional<Discount> applyProductOffer(Product product, int quantity);
    // FIXME we are passing the whole shopping cart which is bad
    Optional<Discount> applyCartOffer(ShoppingCartDto shoppingCart);
}
