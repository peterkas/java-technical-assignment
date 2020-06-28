package kata.supermarket;

import java.math.BigDecimal;

public class ItemByWeight implements Item {

    private final WeighedProduct product;
    private final BigDecimal weightInKilos;
    private final DiscountScheme discountScheme;

    ItemByWeight(final WeighedProduct product, final BigDecimal weightInKilos) {
        this.product = product;
        this.weightInKilos = weightInKilos;
        this.discountScheme = null;
    }

    ItemByWeight(final WeighedProduct product, final BigDecimal weightInKilos, final DiscountScheme discountScheme) {
        this.product = product;
        this.weightInKilos = weightInKilos;
        this.discountScheme = discountScheme;
    }

    public BigDecimal price() {
        return product.pricePerKilo().multiply(weightInKilos).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal discount() {
        if (discountScheme == null) {
            return null;
        }
        BigDecimal discountedKilos = BigDecimal.ZERO;
        if (this.weightInKilos.compareTo(BigDecimal.valueOf(discountScheme.dealQuantity())) >= 0){
            discountedKilos = BigDecimal.valueOf(this.weightInKilos.divide(BigDecimal.valueOf(discountScheme.dealQuantity())).longValue());
        }
        return product.pricePerKilo().multiply(discountScheme.getValue()).multiply(discountedKilos).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public long dealQuantity() {
        return discountScheme.dealQuantity();
    }
}
