package com.ee.cart;

import com.ee.cart.dto.CartItemDto;
import com.ee.offer.OfferService;
import com.ee.product.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartItems {

    private final Map<Product, CartItem> items = new HashMap<>();
    private final OfferService offerService;

    public CartItems(OfferService offerService) {
        this.offerService = offerService;
    }

    public void add(Product product, int quantity) {
        CartItem cartItem;
        if (items.containsKey(product)) {
            CartItem existingCartItem = items.remove(product);
            cartItem = new CartItem(product, existingCartItem.quantity + quantity);
        } else {
            cartItem = new CartItem(product, quantity);
        }

        CartItem discountedCartItem = offerService.applyProductOffer(cartItem.product, cartItem.quantity)
                .map(cartItem::applyDiscount).orElse(cartItem);

        items.put(product, discountedCartItem);
    }

    public BigDecimal totalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : items.values()) {
            totalPrice = totalPrice.add(item.price());
        }
        return totalPrice;
    }

    // FIXME think this through
    public BigDecimal discount() {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (CartItem item : items.values()) {
            totalDiscount = totalDiscount.add(item.discount());
        }
        return totalDiscount.setScale(2, RoundingMode.HALF_UP);
    }

    public List<CartItemDto> toDto() {
        return items.values().stream()
                .map(cartItem -> new CartItemDto(cartItem.product, cartItem.quantity, cartItem.discount()))
                .collect(Collectors.toList());
    }
}
