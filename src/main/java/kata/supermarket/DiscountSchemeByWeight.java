package kata.supermarket;

import java.math.BigDecimal;
import java.util.List;

public class DiscountSchemeByWeight implements DiscountScheme{
    private BigDecimal value;
    private int quantity;

    DiscountSchemeByWeight(BigDecimal value, int quantity) {
        this.value = value;
        this.quantity = quantity;
    }

    public int dealQuantity() { return this.quantity; }

    public BigDecimal getValue(){ return this.value; }

    public BigDecimal apply(List<Item> items) {
        BigDecimal discount = BigDecimal.ZERO;
        if (items == null || items.isEmpty()) {
            return discount;
        }
        // DiscountScheme has already applied when the product was weighted so we only need to sum all item discounts
        discount = items.stream().map(Item::discount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return discount;
    }

}
