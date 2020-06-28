package kata.supermarket;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private final BigDecimal pricePerUnit;

    public Product(final BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    BigDecimal pricePerUnit() {
        return pricePerUnit;
    }

    public Item oneOf() {
        return new ItemByUnit(this);
    }

    public Iterable<Item> anyOf(int quantity) {
        List<Item> items = null;
        if (quantity > 0) {
            items = new ArrayList<>(quantity);
            for (int i = 0; i < quantity; i++) {
                items.add(new ItemByUnit(this));
            }
        }
        return items;
    }
}
