package features;

import com.ee.cart.ShoppingCart;
import features.offer.InMemoryOfferRepository;
import com.ee.offer.OfferService;
import com.ee.offer.OfferServiceImpl;
import com.ee.offer.product.Buy1Get50PercentOffOnNextOffer;
import com.ee.offer.product.Buy2Get1Offer;
import com.ee.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingCartProductOfferFeatureTest {

    public static final Product DOVE_SOAP = new Product("Dove Soap",
            BigDecimal.valueOf(39.99).setScale(2, RoundingMode.HALF_UP));
    private static final Product AXE_DEO = new Product("Axe Deo",
            BigDecimal.valueOf(89.99).setScale(2, RoundingMode.HALF_UP));

    private static final BigDecimal TAX = BigDecimal.valueOf(12.5);
    private ShoppingCart cart;
    private InMemoryOfferRepository offers;

    @BeforeEach
    public void setUp() {
        offers = new InMemoryOfferRepository();
        OfferService offerService = new OfferServiceImpl(offers);
        cart = new ShoppingCart(TAX, offerService);
    }

    @Test
    public void appliesBuy2Get1ProductOfferAvailableOnItemsInCart() {
        offers.addProductOffer(DOVE_SOAP, new Buy2Get1Offer());

        cart.add(DOVE_SOAP, 3);
        cart.add(AXE_DEO, 2);

        assertThat(cart.cartAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(292.46).setScale(2, RoundingMode.HALF_UP));
        assertThat(cart.taxAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(32.50).setScale(2, RoundingMode.HALF_UP));
        assertThat(cart.discount())
                .isEqualByComparingTo(BigDecimal.valueOf(39.99).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void appliesBuy2Get50PercentOffOnNextProductOfferAvailableOnItemsInCart() {
        offers.addProductOffer(DOVE_SOAP, new Buy1Get50PercentOffOnNextOffer());

        cart.add(DOVE_SOAP, 2);

        assertThat(cart.cartAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(67.49).setScale(2, RoundingMode.HALF_UP));
        assertThat(cart.discount())
                .isEqualByComparingTo(BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_UP));
        assertThat(cart.taxAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(7.50).setScale(2, RoundingMode.HALF_UP));
    }
}
