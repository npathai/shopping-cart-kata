package com.ee.offer;

import com.ee.offer.cart.CartOffer;
import com.ee.offer.product.ProductOffer;
import com.ee.product.Product;

import java.util.Optional;

public interface OfferRepository {
    Optional<ProductOffer> getProductOffer(Product product);

    Optional<CartOffer> getCartOffer();
}
