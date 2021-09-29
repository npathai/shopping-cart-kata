package com.ee.offer;

import com.ee.cart.dto.ShoppingCartDto;
import com.ee.offer.cart.CartOffer;
import com.ee.offer.product.ProductOffer;
import com.ee.product.Product;

import java.util.Optional;

public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public Optional<Discount> applyProductOffer(Product product, int quantity) {
        Optional<ProductOffer> productOffer = offerRepository.getProductOffer(product);
        if (!productOffer.isPresent()) {
            return Optional.empty();
        }
        return productOffer.get().apply(product, quantity);
    }

    @Override
    public Optional<Discount> applyCartOffer(ShoppingCartDto shoppingCart) {
        Optional<CartOffer> cartOffer = offerRepository.getCartOffer();
        if (!cartOffer.isPresent()) {
            return Optional.empty();
        }
        return cartOffer.get().apply(shoppingCart);
    }
}
