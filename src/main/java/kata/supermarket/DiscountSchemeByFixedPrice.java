package kata.supermarket;

import java.math.BigDecimal;
import java.util.List;

public class DiscountSchemeByFixedPrice implements DiscountScheme {

    private BigDecimal value;
    private int quantity;

    DiscountSchemeByFixedPrice(BigDecimal value, int quantity) {
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
        if (items.size() >= dealQuantity()) {
            BigDecimal noDiscPrice = items.stream().map(Item::price)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            int numItemsWithNoDiscount = items.size() % dealQuantity();
            int numItemsWithDiscount = items.size() - numItemsWithNoDiscount;
            discount = noDiscPrice.subtract(BigDecimal.valueOf(numItemsWithDiscount / dealQuantity()).multiply(getValue()));
        }
        return discount;
    }

}
