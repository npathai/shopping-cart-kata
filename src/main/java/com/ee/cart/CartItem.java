package com.ee.cart;

import com.ee.offer.Discount;
import com.ee.product.Product;

import java.math.BigDecimal;

public class CartItem {

    public final Product product;
    public final int quantity;
    private final BigDecimal discount;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.discount = BigDecimal.ZERO;
    }

    CartItem(Product product, int quantity, BigDecimal discount) {
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
    }

    public BigDecimal price() {
        return product.unitPrice.multiply(BigDecimal.valueOf(quantity)).subtract(discount);
    }

    public BigDecimal discount() {
        return discount;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public CartItem applyDiscount(Discount discount) {
        return new CartItem(product, quantity, discount.amount);
    }
}
