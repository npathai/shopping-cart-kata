package com.ee.cart;

import com.ee.offer.Discount;
import com.ee.offer.OfferService;
import com.ee.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartProductOfferTest {

    public static final Product DOVE_SOAP = new Product("Dove Soap", BigDecimal.valueOf(10.00));
    private static final Product AXE_DEO = new Product("Axe Deo", BigDecimal.valueOf(20.00));
    public static final BigDecimal TAX = BigDecimal.valueOf(10);

    @Mock
    OfferService offerService;
    ShoppingCart cart;

    @BeforeEach
    public void setUp() {
        cart = new ShoppingCart(TAX, offerService);
    }

    @Nested
    public class ProductOfferAvailable {

        @BeforeEach
        public void setUp() {
            when(offerService.applyProductOffer(DOVE_SOAP, 2))
                    .thenReturn(Optional.of(new Discount(BigDecimal.valueOf(10))));

            cart.add(DOVE_SOAP, 2);
        }

        @Test
        public void returnsCartTotalAfterReducingCartItemPriceByDiscountAmount() {
            assertBigDecimalEquals(cart.cartAmount(), 11.00);
        }

        @Test
        public void returnsTaxAfterReducingCartItemPriceByDiscountAmount() {
            assertBigDecimalEquals(cart.taxAmount(), 1.00);
        }

        @Test
        public void returnsDiscountAmount() {
            assertBigDecimalEquals(cart.discount(), 10.00);
        }
    }

    @Nested
    public class ProductOfferNotApplicable {

        @BeforeEach
        public void setUp() {
            when(offerService.applyProductOffer(DOVE_SOAP, 2))
                    .thenReturn(Optional.empty());

            cart.add(DOVE_SOAP, 2);
        }

        @Test
        public void returnsCartTotalWithoutApplyingDiscount() {
            assertBigDecimalEquals(cart.cartAmount(), 22.00);
        }

        @Test
        public void returnsTaxAmountWithoutApplyingDiscount() {
            assertBigDecimalEquals(cart.taxAmount(), 2.00);
        }

        @Test
        public void returnsDiscountAmountAsZero() {
            assertBigDecimalEquals(cart.discount(), 0.00);
        }
    }

    @Nested
    public class MultipleProducts {

        @Nested
        public class ProductOfferApplicableOnSingle {

            @BeforeEach
            public void setUp() {
                when(offerService.applyProductOffer(DOVE_SOAP, 2))
                        .thenReturn(Optional.of(new Discount(BigDecimal.valueOf(10))));
                when(offerService.applyProductOffer(AXE_DEO, 2))
                        .thenReturn(Optional.empty());

                cart.add(DOVE_SOAP, 2);
                cart.add(AXE_DEO, 2);
            }

            @Test
            public void returnsCartAmountOnlyAfterApplyingDiscountForProductWithAvailableDiscount() {
                assertBigDecimalEquals(cart.cartAmount(), 55.00);
            }

            @Test
            public void returnsTaxAmountOnlyAfterApplyingDiscountForProductWithAvailableDiscount() {
                assertBigDecimalEquals(cart.taxAmount(), 5.00);
            }

            @Test
            public void returnsDiscountOnlyAfterApplyingDiscountForProductWithAvailableDiscount() {
                assertBigDecimalEquals(cart.discount(), 10.00);
            }
        }

        @Nested
        public class ProductOfferApplicableOnAllProducts {

            @BeforeEach
            public void setUp() {
                when(offerService.applyProductOffer(DOVE_SOAP, 2))
                        .thenReturn(Optional.of(new Discount(BigDecimal.valueOf(10))));
                when(offerService.applyProductOffer(AXE_DEO, 2))
                        .thenReturn(Optional.of(new Discount(BigDecimal.valueOf(15))));

                cart.add(DOVE_SOAP, 2);
                cart.add(AXE_DEO, 2);
            }

            @Test
            public void returnsCartAmountOnlyAfterApplyingDiscountForProductWithAvailableDiscount() {
                assertBigDecimalEquals(cart.cartAmount(), 38.50);
            }

            @Test
            public void returnsTaxAmountOnlyAfterApplyingDiscountForProductWithAvailableDiscount() {
                assertBigDecimalEquals(cart.taxAmount(), 3.50);
            }

            @Test
            public void returnsDiscountOnlyAfterApplyingDiscountForProductWithAvailableDiscount() {
                assertBigDecimalEquals(cart.discount(), 25.00);
            }
        }
    }

    private void assertBigDecimalEquals(BigDecimal actual, double expected) {
        assertThat(actual).isEqualByComparingTo(BigDecimal.valueOf(expected).setScale(2, RoundingMode.HALF_UP));
    }
}
