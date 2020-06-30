package kata.supermarket;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DiscountSchemeByVolume implements DiscountScheme {

    private BigDecimal value;
    private int quantity;

    DiscountSchemeByVolume(BigDecimal value, int quantity) {
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
            Iterator<Item> it = items.iterator();
            int disItems = 0;
            BigDecimal discPrice = BigDecimal.ZERO;
            while (disItems <= numItemsWithDiscount && it.hasNext()) {
                List<Item> dealItems = new ArrayList<>();
                for (int i = 0; i < dealQuantity(); i++) {
                    dealItems.add(it.next());
                    disItems++;
                }
                // Sort items by price and apply discount to the cheapest
                BigDecimal dealPrice = dealItems.stream()
                        .sorted(Comparator.comparing(Item::price).reversed())
                        .collect(Collectors.toList()).stream()
                        .limit(getValue().longValue()).map(Item::price)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);
                discPrice = discPrice.add(dealPrice);
            }
            discount = noDiscPrice.subtract(discPrice);
        }
        return discount;
    }

}
