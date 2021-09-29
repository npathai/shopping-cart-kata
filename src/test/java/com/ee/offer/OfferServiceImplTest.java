package com.ee.offer;

import com.ee.cart.dto.ShoppingCartDto;
import com.ee.offer.cart.CartOffer;
import com.ee.offer.product.ProductOffer;
import com.ee.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {
    public static final Product PRODUCT = new Product("Any", BigDecimal.valueOf(10.00));

    @Mock
    OfferRepository offerRepository;
    OfferServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new OfferServiceImpl(offerRepository);
    }

    @Test
    public void returnsEmptyWhenProductOfferIsNotPresent() {
        when(offerRepository.getProductOffer(Mockito.any())).thenReturn(Optional.empty());

        assertThat(service.applyProductOffer(PRODUCT, anyInt()))
                .isEmpty();
    }

    @Test
    public void appliesProductOfferWhenPresent() {
        ProductOffer mockOffer = mock(ProductOffer.class);
        when(offerRepository.getProductOffer(PRODUCT)).thenReturn(Optional.of(mockOffer));

        service.applyProductOffer(PRODUCT, 1);

        verify(mockOffer).apply(PRODUCT, 1);
    }

    @Test
    public void returnsEmptyWhenCartOfferIsNotPresent() {
        when(offerRepository.getCartOffer()).thenReturn(Optional.empty());

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(BigDecimal.ONE, Collections.emptyList());
        assertThat(service.applyCartOffer(shoppingCartDto)).isEmpty();
    }

    @Test
    public void appliesCartOfferWhenPresent() {
        CartOffer mockOffer = mock(CartOffer.class);
        when(offerRepository.getCartOffer()).thenReturn(Optional.of(mockOffer));

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(BigDecimal.ONE, Collections.emptyList());
        service.applyCartOffer(shoppingCartDto);

        verify(mockOffer).apply(shoppingCartDto);
    }
}