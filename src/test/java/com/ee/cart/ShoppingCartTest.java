package com.ee.cart;

import com.ee.cart.ShoppingCart;
import com.ee.cart.dto.CartItemDto;
import com.ee.offer.OfferRepository;
import com.ee.offer.OfferService;
import com.ee.offer.OfferServiceImpl;
import com.ee.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartTest {

    public static final Product DOVE_SOAP = new Product("Dove Soap", BigDecimal.valueOf(39.99));
    private static final Product AXE_DEO = new Product("Axe Deo", BigDecimal.valueOf(99.99));
    private ShoppingCart cart;

    @Mock(lenient = true)
    OfferService offerService;

    @BeforeEach
    public void setUp() {
        cart = new ShoppingCart(BigDecimal.ZERO, offerService);

        when(offerService.applyProductOffer(any(), anyInt())).thenReturn(Optional.empty());
        when(offerService.applyCartOffer(any())).thenReturn(Optional.empty());
    }

    @Nested
    public class AddingAProduct {

        @Test
        public void throwsNullPointerExceptionWhenProductIsNull() {
            assertThatThrownBy(() -> cart.add(null, 1)).isInstanceOf(NullPointerException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                -2
                -1,
                0
        })
        public void throwsIllegalArgumentExceptionWhenQuantityIsNotPositive(int quantity) {
            assertThatThrownBy(() -> cart.add(DOVE_SOAP, quantity))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void addAnItemToTheShoppingCartShouldProvideTheCartItem() {
            cart.add(DOVE_SOAP, 5);

            assertThat(cart.getItems())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(
                        nonDiscountedItem(new Product("Dove Soap", BigDecimal.valueOf(39.99)), 5)
                    );
        }

        @Test
        public void addingDuplicateItemsToTheShoppingCartCombinesTheItemsInASingleItem() {
            cart.add(DOVE_SOAP, 5);
            cart.add(DOVE_SOAP, 3);

            assertThat(cart.getItems())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(
                        nonDiscountedItem(new Product("Dove Soap", BigDecimal.valueOf(39.99)), 8)
                    );
        }
    }

    private static CartItemDto nonDiscountedItem(Product product, int quantity) {
        return new CartItemDto(product, quantity, BigDecimal.ZERO);
    }

    @Nested
    public class TotalOfTheCart {

        @Test
        public void returnsTheTotalPriceOfAllItemsInTheCart() {
            cart.add(DOVE_SOAP, 5);

            assertThat(cart.cartAmount()).isEqualTo(BigDecimal.valueOf(199.95));
        }

        @Test
        public void addingDuplicateItemsToTheShoppingCartGivesCorrectTotal() {
            cart.add(DOVE_SOAP, 5);
            cart.add(DOVE_SOAP, 3);

            assertThat(cart.cartAmount()).isEqualTo(BigDecimal.valueOf(319.92));
        }

        @Nested
        public class TaxApplicable {

            @BeforeEach
            public void setUp() {
                cart = new ShoppingCart(BigDecimal.valueOf(12.5), offerService);
            }

            @Test
            public void addsTheTotalTaxAmountToCartTotalPrice() {
                cart.add(DOVE_SOAP, 2);
                cart.add(AXE_DEO, 2);

                assertThat(cart.cartAmount()).isEqualTo(BigDecimal.valueOf(314.96));
            }
            
            @Test
            public void returnsTotalTaxApplicable() {
                cart.add(DOVE_SOAP, 2);
                cart.add(AXE_DEO, 2);

                assertThat(cart.taxAmount()).isEqualTo(BigDecimal.valueOf(35.0).setScale(2, RoundingMode.HALF_UP));
            }
        }
    }

}
