package kata.supermarket;

import java.math.BigDecimal;
import java.util.List;

public class DefaultDiscountScheme implements DiscountScheme{
    private BigDecimal value;
    private int quantity;

    DefaultDiscountScheme(BigDecimal value, int quantity) {
        this.value = value;
        this.quantity = quantity;
    }

    public int dealQuantity() { return this.quantity; }

    public BigDecimal getValue(){ return this.value; }

    public BigDecimal apply(List<Item> items) {
        // No Discount by Default
        return BigDecimal.ZERO;
    }

}
