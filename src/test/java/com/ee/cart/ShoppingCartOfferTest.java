package com.ee.cart;

import com.ee.cart.ShoppingCart;
import com.ee.cart.dto.ShoppingCartDto;
import com.ee.offer.Discount;
import com.ee.offer.OfferService;
import com.ee.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartOfferTest {

    public static final Product DOVE_SOAP =
            new Product("Dove Soap", BigDecimal.valueOf(39.99).setScale(2, RoundingMode.HALF_UP));
    private static final Product AXE_DEO =
            new Product("Axe Deo", BigDecimal.valueOf(89.99).setScale(2, RoundingMode.HALF_UP));

    private ShoppingCart cart;

    @Mock
    OfferService offerService;

    @BeforeEach
    public void setUp() {
        cart = new ShoppingCart(BigDecimal.valueOf(12.50).setScale(2, RoundingMode.HALF_UP), offerService);
        when(offerService.applyProductOffer(any(Product.class), anyInt())).thenReturn(Optional.empty());

        when(offerService.applyCartOffer(any(ShoppingCartDto.class)))
                .thenReturn(Optional.of(new Discount(BigDecimal.valueOf(111.98))));

        cart.add(DOVE_SOAP, 5);
        cart.add(AXE_DEO, 4);
    }

    @Test
    public void returnsDiscountedCartAmountAfterApplyingCartOffer() {
        assertBigDecimalEquals(cart.cartAmount(), 503.93);
    }

    @Test
    public void returnsTaxAmountApplicableOnDiscountedCartAmount() {
        assertBigDecimalEquals(cart.taxAmount(), 56.00);
    }

    @Test
    public void returnsDiscountApplied() {
        assertBigDecimalEquals(cart.discount(), 111.98);
    }

    private void assertBigDecimalEquals(BigDecimal actual, double expected) {
        assertThat(actual).isEqualByComparingTo(BigDecimal.valueOf(expected).setScale(2, RoundingMode.HALF_UP));
    }

}
