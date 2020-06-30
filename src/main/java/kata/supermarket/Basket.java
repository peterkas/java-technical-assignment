package kata.supermarket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Basket {
    private static final DiscountScheme NO_DISCOUNT = new DefaultDiscountScheme(BigDecimal.ONE,1);

    private final Map<DiscountScheme,List<Item>> items;

    public Basket() {
        this.items = new HashMap<>();
    }

    public void add(final Item item) {
        add(item, NO_DISCOUNT);
    }

    public void add(final Item item, final DiscountScheme discountScheme) {
        List<Item> itemList = items.get(discountScheme)!=null ? items.get(discountScheme) : new ArrayList<>();
        itemList.add(item);
        this.items.put(discountScheme, itemList);
    }

    Map<DiscountScheme,List<Item>> items() {
        return Collections.unmodifiableMap(this.items);
    }

    public BigDecimal total() {
        return new TotalCalculator().calculate();
    }

    private class TotalCalculator {
        private final Map<DiscountScheme,List<Item>> items;

        TotalCalculator() {
            this.items = items();
        }

        private BigDecimal subtotal() {
            List<Item> allItems = this.items.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            return allItems.stream().map(Item::price)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        private BigDecimal discounts() {

            BigDecimal discountsToApply = BigDecimal.ZERO;
            for (DiscountScheme discountScheme : this.items.keySet()) {
                discountsToApply = discountsToApply.add(discountScheme.apply(items.get(discountScheme)));
            }
            return discountsToApply;
        }

        private BigDecimal calculate() {
            return subtotal().subtract(discounts());
        }
    }
}
