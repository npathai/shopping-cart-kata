package com.ee.offer.product;

import com.ee.offer.Discount;
import com.ee.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Buy1Get50PercentOffOnNextOfferTest {
    private static final Product PRODUCT = new Product("Any", BigDecimal.valueOf(12.00));

    private Buy1Get50PercentOffOnNextOffer offer;

    @BeforeEach
    public void setUp() {
        offer = new Buy1Get50PercentOffOnNextOffer();
    }

    @Test
    public void throwsIllegalArgumentExceptionWhenQuantityIsZero() {
        assertThatThrownBy(() -> offer.apply(PRODUCT, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void isNotApplicableForSingleQuantity() {
        assertThat(offer.apply(PRODUCT, 1))
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("dataFor_forEvery1Applies50PercentOnNext")
    public void forEvery1Applies50PercentOnNext(int quantity, BigDecimal expectedDiscount) {
        assertThat(offer.apply(PRODUCT, quantity))
                .hasValue(new Discount(expectedDiscount.setScale(2, RoundingMode.HALF_UP)));
    }

    public static Stream<Arguments> dataFor_forEvery1Applies50PercentOnNext() {
        return Stream.of(
                Arguments.of(2, BigDecimal.valueOf(6.00)),
                Arguments.of(3, BigDecimal.valueOf(6.00)),
                Arguments.of(4, BigDecimal.valueOf(12.00)),
                Arguments.of(5, BigDecimal.valueOf(12.00))
        );
    }
}