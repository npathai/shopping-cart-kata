package features;

import com.ee.cart.ShoppingCart;
import features.offer.InMemoryOfferRepository;
import com.ee.offer.OfferService;
import com.ee.offer.OfferServiceImpl;
import com.ee.offer.cart.Flat20PercentOffOffer;
import com.ee.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingCartOfferFeatureTest {
    
    public static final Product DOVE_SOAP = new Product("Dove Soap",
            BigDecimal.valueOf(39.99).setScale(2, RoundingMode.HALF_UP));
    private static final Product AXE_DEO = new Product("Axe Deo",
            BigDecimal.valueOf(89.99).setScale(2, RoundingMode.HALF_UP));

    private static final BigDecimal TAX = BigDecimal.valueOf(12.5);
    private ShoppingCart cart;

    @BeforeEach
    public void setUp() {
        InMemoryOfferRepository offers = new InMemoryOfferRepository();
        offers.setCartOffer(new Flat20PercentOffOffer());
        OfferService offerService = new OfferServiceImpl(offers);
        cart = new ShoppingCart(TAX, offerService);
    }
    
    @Test
    public void appliesFlat20PercentOffCartOffer() {
        cart.add(DOVE_SOAP, 5);
        cart.add(AXE_DEO, 4);

        assertThat(cart.cartAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(503.93).setScale(2, RoundingMode.HALF_UP));
        assertThat(cart.taxAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(56.00).setScale(2, RoundingMode.HALF_UP));
        assertThat(cart.discount())
                .isEqualByComparingTo(BigDecimal.valueOf(111.98).setScale(2, RoundingMode.HALF_UP));
    }
}
