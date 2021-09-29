package features.offer;

import com.ee.offer.OfferRepository;
import com.ee.offer.cart.CartOffer;
import com.ee.offer.product.ProductOffer;
import com.ee.product.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryOfferRepository implements OfferRepository {
    private final Map<Product, ProductOffer> productWiseOffer = new HashMap<>();
    private CartOffer cartOffer;

    public void addProductOffer(Product product, ProductOffer offer) {
        productWiseOffer.put(product, offer);
    }

    public void setCartOffer(CartOffer cartOffer) {
        this.cartOffer = cartOffer;
    }

    @Override
    public Optional<ProductOffer> getProductOffer(Product product) {
        return Optional.ofNullable(productWiseOffer.get(product));
    }

    @Override
    public Optional<CartOffer> getCartOffer() {
        return Optional.ofNullable(cartOffer);
    }
}
