package com.ee.cart.dto;

import com.ee.product.Product;

import java.math.BigDecimal;

public class CartItemDto {
    public final Product product;
    public final int quantity;
    public final BigDecimal discount;

    public CartItemDto(Product product, int quantity, BigDecimal discount) {
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
    }
}
