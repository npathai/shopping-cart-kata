package com.ee.cart;

import java.math.BigDecimal;

class CartAmount {

    private final BigDecimal cartTotal;
    private final BigDecimal cartDiscount;

    public CartAmount(BigDecimal cartTotal, BigDecimal cartDiscount) {
        this.cartTotal = cartTotal;
        this.cartDiscount = cartDiscount;
    }

    public BigDecimal getDiscountedTotal() {
        return cartTotal
                .subtract(cartDiscount);
    }

    public BigDecimal getCartDiscount() {
        return cartDiscount;
    }
}
