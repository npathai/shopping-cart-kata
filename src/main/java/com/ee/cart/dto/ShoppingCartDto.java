package com.ee.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartDto {
    public final BigDecimal totalExcludingTax;
    public final List<CartItemDto> cartItems;

    public ShoppingCartDto(BigDecimal totalExcludingTax, List<CartItemDto> cartItems) {
        this.totalExcludingTax = totalExcludingTax;
        this.cartItems = cartItems;
    }
}
