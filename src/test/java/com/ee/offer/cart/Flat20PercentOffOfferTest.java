package com.ee.offer.cart;

import com.ee.cart.dto.ShoppingCartDto;
import com.ee.offer.Discount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Flat20PercentOffOfferTest {

    private Flat20PercentOffOffer offer;

    @BeforeEach
    public void setUp() {
        offer = new Flat20PercentOffOffer();
    }

    @ParameterizedTest
    @MethodSource("dataFor_returns20PercentOfCartAmountWhenCartAmountIsGreaterThanOrEqualTo500")
    public void returns20PercentOfCartAmountWhenCartAmountIsGreaterThanOrEqualTo500(BigDecimal cartAmount,
                                                                                    BigDecimal expectedDiscount) {
        ShoppingCartDto dto = new ShoppingCartDto(cartAmount, Collections.emptyList());
        assertThat(offer.apply(dto))
                .hasValue(new Discount(expectedDiscount.setScale(2, RoundingMode.HALF_UP)));
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> dataFor_returns20PercentOfCartAmountWhenCartAmountIsGreaterThanOrEqualTo500() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(500.00), BigDecimal.valueOf(100.00)),
                Arguments.of(BigDecimal.valueOf(501.00), BigDecimal.valueOf(100.20))
        );
    }

    @ParameterizedTest
    @MethodSource("dataFor_returnsEmptyOptionalWhenCartAmountIsLessThan500")
    public void returnsEmptyOptionalWhenCartAmountIsLessThan500(BigDecimal cartAmount) {
        ShoppingCartDto dto = new ShoppingCartDto(cartAmount, Collections.emptyList());

        assertThat(offer.apply(dto)).isEmpty();
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> dataFor_returnsEmptyOptionalWhenCartAmountIsLessThan500() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(499.00)),
                Arguments.of(BigDecimal.valueOf(498.00))
        );
    }
}