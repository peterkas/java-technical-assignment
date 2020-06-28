package kata.supermarket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasketTest {

    @DisplayName("basket provides its total value when containing...")
    @MethodSource
    @ParameterizedTest(name = "{0}")
    void basketProvidesTotalValue(String description, String expectedTotal, Iterable<Item> items, DiscountScheme discountScheme) {
        final Basket basket = new Basket();
        items.forEach(i -> basket.add(i, discountScheme));
        assertEquals(new BigDecimal(expectedTotal), basket.total());
    }

    static Stream<Arguments> basketProvidesTotalValue() {
        return Stream.of(
                noItems(),
                aSingleItemPricedPerUnit(),
                multipleItemsPricedPerUnit(),
                aSingleItemPricedByWeight(),
                multipleItemsPricedByWeight(),
                multipleItemsPricedByWeight(DiscountScheme.ONE_KG_HALF_PRICE)
        );
    }

    private static Arguments aSingleItemPricedByWeight() {
        return Arguments.of("a single weighed item", "1.25", Collections.singleton(twoFiftyGramsOfAmericanSweets()),
                DiscountScheme.NO_DISCOUNT);
    }

    private static Arguments multipleItemsPricedByWeight() {
        return Arguments.of("multiple weighed items", "1.85",
                Arrays.asList(twoFiftyGramsOfAmericanSweets(), twoHundredGramsOfPickAndMix()),
                DiscountScheme.NO_DISCOUNT
        );
    }

    private static Arguments multipleItemsPricedByWeight(DiscountScheme discountScheme) {
        return Arguments.of("multiple weighed items with discount", "6.84",
                Arrays.asList(twoKilosAndTwoFiftyGramsOfAmericanSweets(discountScheme), twoHundredGramsOfPickAndMix(discountScheme)),
                discountScheme
        );
    }

    private static Arguments multipleItemsPricedPerUnit() {
        return Arguments.of("multiple items priced per unit", "2.04",
                Arrays.asList(aPackOfDigestives(), aPintOfMilk()), DiscountScheme.NO_DISCOUNT);
    }

    private static Arguments aSingleItemPricedPerUnit() {
        return Arguments.of("a single item priced per unit", "0.49", Collections.singleton(aPintOfMilk()), DiscountScheme.NO_DISCOUNT);
    }

    private static Arguments noItems() {
        return Arguments.of("no items", "0.00", Collections.emptyList(), DiscountScheme.NO_DISCOUNT);
    }

    private static Item aPintOfMilk() {
        return new Product(new BigDecimal("0.49")).oneOf();
    }

    private static Item aPackOfDigestives() {
        return new Product(new BigDecimal("1.55")).oneOf();
    }

    private static WeighedProduct aKiloOfAmericanSweets() {
        return new WeighedProduct(new BigDecimal("4.99"));
    }

    private static Item twoFiftyGramsOfAmericanSweets() {
        return aKiloOfAmericanSweets().weighing(new BigDecimal(".25"));
    }

    private static Item twoKilosAndTwoFiftyGramsOfAmericanSweets(DiscountScheme discountScheme) {
        return aKiloOfAmericanSweets().weighing(new BigDecimal("2.25"), discountScheme);
    }

    private static WeighedProduct aKiloOfPickAndMix() {
        return new WeighedProduct(new BigDecimal("2.99"));
    }

    private static Item twoHundredGramsOfPickAndMix() {
        return aKiloOfPickAndMix().weighing(new BigDecimal(".2"));
    }

    private static Item twoHundredGramsOfPickAndMix(DiscountScheme discountScheme) {
        return aKiloOfPickAndMix().weighing(new BigDecimal(".2"), discountScheme);
    }
}