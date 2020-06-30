package kata.supermarket;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Pricing discount schemes
 */
public interface DiscountScheme {

    int dealQuantity();

    BigDecimal getValue();

    BigDecimal apply(List<Item> items);
}
