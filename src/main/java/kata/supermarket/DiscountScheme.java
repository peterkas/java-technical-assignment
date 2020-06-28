package kata.supermarket;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Pricing discount schemes
 */
public enum DiscountScheme {

    NO_DISCOUNT(DiscountType.NO_DISCOUNT, BigDecimal.ONE, 1),
    BUY_ONE_GET_ONE(DiscountType.PER_VOLUME, BigDecimal.ONE, 2),
    TWO_FOR_ONE_POUND(DiscountType.FIXED_PRICE, BigDecimal.ONE, 2),
    THREE_FOR_TWO(DiscountType.PER_VOLUME, BigDecimal.valueOf(2), 3),
    ONE_KG_HALF_PRICE(DiscountType.PER_WEIGHT, BigDecimal.valueOf(0.5),1);

    private DiscountType type;
    private BigDecimal value;
    private int quantity;

    DiscountScheme(DiscountType type, BigDecimal value, int quantity) {
        this.type = type;
        this.value = value;
        this.quantity = quantity;
    }

    int dealQuantity() { return this.quantity; }

    BigDecimal getValue(){ return this.value; }

    BigDecimal apply(List<Item> items) {
        BigDecimal discount = BigDecimal.ZERO;
        if (items == null || items.isEmpty()) {
            return discount;
        }
        switch (this.type) {
            case FIXED_PRICE:
                if (items.size() >= dealQuantity()) {
                    // TODO Calculate discount based on Fixed Price every dealQuantity() units
                }
                break;
            case PER_VOLUME:
                if (items.size() >= dealQuantity()) {
                    // Price without any discount
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
                break;
            case PER_WEIGHT:
                // DiscountScheme has already applied when the product was weighted so we only need to sum all item discounts
                discount = items.stream().map(Item::discount)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);
                break;
            case NO_DISCOUNT:
                break;
            default:

        }
        return discount;
    }

}
