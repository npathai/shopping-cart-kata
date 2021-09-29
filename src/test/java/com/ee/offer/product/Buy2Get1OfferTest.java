package com.ee.offer.product;

import com.ee.offer.Discount;
import com.ee.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Buy2Get1OfferTest {
    private static final Product PRODUCT = new Product("Any",
            BigDecimal.valueOf(12.00).setScale(2, RoundingMode.HALF_UP));

    private Buy2Get1Offer offer;

    @BeforeEach
    public void setUp() {
        offer = new Buy2Get1Offer();
    }

    @Test
    public void throwsIllegalArgumentExceptionWhenQuantityIsZero() {
        assertThatThrownBy(() -> offer.apply(PRODUCT, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1,
            2
    })
    public void returnsEmptyWhenQuantityIsNotSufficient(int quantity) {
        assertThat(offer.apply(PRODUCT, quantity))
                .isEmpty();
    }

    @ParameterizedTest
    @MethodSource("dataFor_forEvery2MakesNextFree")
    public void forEvery2MakesNextFree(int quantity, BigDecimal expectedDiscount) {
        assertThat(offer.apply(PRODUCT, quantity))
                .hasValue(new Discount(expectedDiscount.setScale(2, RoundingMode.HALF_UP)));
    }

    @SuppressWarnings("unused")
    public static Stream<Arguments> dataFor_forEvery2MakesNextFree() {
        return Stream.of(
                Arguments.of(3, BigDecimal.valueOf(12.00)),
                Arguments.of(4, BigDecimal.valueOf(12.00)),
                Arguments.of(5, BigDecimal.valueOf(12.00)),
                Arguments.of(6, BigDecimal.valueOf(24.00)),
                Arguments.of(7, BigDecimal.valueOf(24.00))
        );
    }



}