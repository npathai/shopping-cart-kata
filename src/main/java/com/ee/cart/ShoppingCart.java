package com.ee.cart;

import com.ee.cart.dto.CartItemDto;
import com.ee.cart.dto.ShoppingCartDto;
import com.ee.offer.OfferService;
import com.ee.product.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.ee.util.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class ShoppingCart {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private final CartItems cartItems;
    private final BigDecimal tax;
    private final OfferService offerService;
    private CartAmount cartAmount;

    public ShoppingCart(BigDecimal tax, OfferService offerService) {
        this.tax = tax;
        this.cartItems = new CartItems(offerService);
        this.offerService = offerService;
        this.cartAmount = new CartAmount(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public void add(Product product, int quantity) {
        requireNonNull(product);
        checkArgument(quantity > 0, "Quantity should be greater than 0");

        cartItems.add(product, quantity);
        cartAmount = updateCartAmount();
    }

    private CartAmount updateCartAmount() {
        BigDecimal currentAmountExcludingTax = cartItems.totalPrice();
        BigDecimal cartDiscount = calculateCartDiscount(currentAmountExcludingTax);
        return new CartAmount(currentAmountExcludingTax, cartDiscount);
    }

    private BigDecimal calculateCartDiscount(BigDecimal totalExcludingTax) {
        return offerService.applyCartOffer(toDto(totalExcludingTax, cartItems.toDto()))
                .map(discount -> discount.amount).orElse(BigDecimal.ZERO);
    }

    private static ShoppingCartDto toDto(BigDecimal totalExcludingTax, List<CartItemDto> cartItemDto) {
        return new ShoppingCartDto(totalExcludingTax, cartItemDto);
    }

    public List<CartItemDto> getItems() {
        return cartItems.toDto();
    }

    public BigDecimal cartAmount() {
        return cartAmount.getDiscountedTotal()
                .add(taxAmount())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal taxAmount() {
        return cartAmount.getDiscountedTotal()
                .multiply(tax).divide(ONE_HUNDRED, 2, RoundingMode.UP);
    }

    // TODO there are no scenarios which suggest that both discounts can co-exist
    public BigDecimal discount() {
        return cartItems.discount()
                .add(cartAmount.getCartDiscount())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
